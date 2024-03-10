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
fun CreditCardScreen(
    onBackClick: () -> Unit
) {

    val viewModel: CreditViewModel = koinViewModel()
    val uiState by viewModel.account.collectAsState()

    var dialogState: CreditAccountEventDialog? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effects.collect {
            when (it) {
                is CreditAccountCardEffect.NavigateBack -> onBackClick()
            }
        }
    }

    CreditCardScreenStateless(
        uiState = uiState,
        dialogState = dialogState,
        changeDialogState = { eventClicked -> dialogState = eventClicked },
        onBackClick = onBackClick,
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
    uiState: Pair<Loan, Account>?,
    onBackClick: () -> Unit,
    dialogState: CreditAccountEventDialog?,
    changeDialogState: (CreditAccountEventDialog?) -> Unit,
    moneyOperation: (Double) -> Unit = {},
    transferMoney: (Double, String) -> Unit
) {
    Scaffold(
        topBar = {
            SingleLevelAppBar(
                title = "Информация по счёту",
                onBackClick = onBackClick
            )
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
                        Text(text = "id ${second.id}")
                    }

                    Text(text = "Баланс ${second.balance / 100f} руб.")
                    Text(text = "Процентная ставка ${first.tariff.interestRate}%")
                    Text(text = "Длительность кредита ${first.loanTermInDays} дня(ей)")
                    Text(text = "Изначальная сумма ${first.amountLoan / 100f} руб.")
                    Text(text = "Задолжность ${first.amountDebt / 100f} руб.")
                    Text(text = "Начисленные пени ${first.accruedPenny / 100f} руб.")
                    Text(text = second.type.toString())
                    Text(text = "Дата создания " + Date(second.creationDate.seconds).convertToReadableTimeLess())
                    if (second.closingDate.seconds != 0L)
                        Text(text = "Дата закрытия `" + Date(second.closingDate.seconds).convertToReadableTimeLess())
                    Text(text = "Счёт заблокирован  ${second.isBlocked}")
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { changeDialogState(CreditAccountEventDialog.Transfer) }) {
                Text(text = "Перевести")
            }

        }
    }
}