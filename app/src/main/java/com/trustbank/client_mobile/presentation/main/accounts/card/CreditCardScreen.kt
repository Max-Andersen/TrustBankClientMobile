package com.trustbank.client_mobile.presentation.main.accounts.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.utils.OutlinedDoubleField
import com.trustbank.client_mobile.presentation.ui.utils.convertToReadableTimeLess
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.Loan
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun CreditCardScreen(onBackClick: () -> Boolean) {

    val viewModel: CreditViewModel = koinViewModel()
    val uiState by viewModel.loan.collectAsState()

    var dialogState: CreditAccountEventDialog? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effects.collect {
            when (it) {
                is AccountCardEffect.NavigateBack -> onBackClick()
            }
        }
    }

    CreditCardScreenStateless(
        uiState = uiState,
        dialogState = dialogState,
        changeDialogState = { eventClicked -> dialogState = eventClicked },
        onBackClick = onBackClick,
        closeAccount = remember { { viewModel.closeAccount() } },
        moneyOperation = { amount ->
            when (dialogState) {
                is CreditAccountEventDialog.Deposit -> viewModel.deposit(amount)
                is CreditAccountEventDialog.Withdraw -> viewModel.withdraw(amount)
                else -> {}
            }
        },
        transferMoney = { amount, toAccount ->
            viewModel.transfer(amount, toAccount)
        }
    )
}

private sealed class CreditAccountEventDialog {
    data object Deposit : CreditAccountEventDialog()

    data object Withdraw : CreditAccountEventDialog()

    data object Transfer : CreditAccountEventDialog()
}

@Composable
private fun CreditCardScreenStateless(
    uiState: Loan?,
    onBackClick: () -> Unit,
    closeAccount: () -> Unit = {},
    dialogState: CreditAccountEventDialog?,
    changeDialogState: (CreditAccountEventDialog?) -> Unit,
    moneyOperation: (Double) -> Unit = {},
    transferMoney: (Double, String) -> Unit
) {
    Scaffold(
        topBar = {
            SingleLevelAppBar(
                title = "Информация по счёту",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { closeAccount() }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                    }
                })
        }
    ) { paddings ->


        dialogState?.let {
            var dialogTitle = ""
            var dialogText = ""
            var inputNumber: Double? by remember { mutableStateOf(null) }


            var toAccount: String by remember { mutableStateOf("") }

            when (it) {
                is CreditAccountEventDialog.Deposit -> {
                    dialogTitle = "Пополнение счёта"
                    dialogText = "Введите сумму для пополнения"
                }

                is CreditAccountEventDialog.Withdraw -> {
                    dialogTitle = "Вывод средств"
                    dialogText = "Введите сумму для вывода"
                }

                is CreditAccountEventDialog.Transfer -> {
                    dialogTitle = "Перевод средств"
                    dialogText = "Введите сумму для перевода"
                }
            }


            AlertDialog(
                icon = {
//                    Icon(icon, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = dialogTitle)
                },
                text = {
                    Column {
                        Text(text = dialogText)
                        OutlinedDoubleField(
                            value = inputNumber,
                            label = "",
                            onValueChange = { newNumber -> inputNumber = newNumber },
                        )

                        if (it is CreditAccountEventDialog.Transfer) {
                            TextField(
                                value = toAccount,
                                onValueChange = { toAccount = it },
                                label = { Text("Счёт получателя") })
                        }

                    }
                },
                onDismissRequest = {
                    changeDialogState(null)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            inputNumber?.let { value ->
                                if (it is CreditAccountEventDialog.Transfer) {
                                    transferMoney(value, toAccount)
                                } else {
                                    moneyOperation(value)
                                }
                                changeDialogState(null)
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }

        Column(
            modifier = Modifier.padding(
                top = paddings.calculateTopPadding() + PADDING_BIG,
                start = PADDING_BIG,
                end = PADDING_BIG,
                bottom = paddings.calculateBottomPadding() + PADDING_BIG
            )
        ) {
            uiState?.let {
                with(uiState) {
                    SelectionContainer {
                        Text(text = "id $id")
                    }

                    Text(text = "Баланс ${balance / 100f} руб.")
                    Text(text = type.toString())
                    Text(text = "Дата создания " + Date(creationDate.seconds).convertToReadableTimeLess())
                    if (closingDate.seconds != 0L)
                        Text(text = "Дата закрытия `" + Date(closingDate.seconds).convertToReadableTimeLess())
                    Text(text = "Счёт заблокирован  $isBlocked")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { changeDialogState(CreditAccountEventDialog.Withdraw) }) {
                Text(text = "Вывести")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { changeDialogState(CreditAccountEventDialog.Deposit) }) {
                Text(text = "Пополнить")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { changeDialogState(CreditAccountEventDialog.Transfer) }) {
                Text(text = "Перевести")
            }

        }
    }
}