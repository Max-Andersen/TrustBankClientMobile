package com.trustbank.client_mobile.presentation.main.accounts.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType.Companion
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import com.trustbank.client_mobile.presentation.ui.utils.OutlinedDoubleField
import com.trustbank.client_mobile.presentation.ui.utils.convertToReadableTimeLess
import com.trustbank.client_mobile.proto.Account
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun AccountCardScreen(
    navController: NavHostController,
    onBackClick: () -> Unit
) {

    val viewModel: AccountCardViewModel = koinViewModel()
    val uiState by viewModel.account.collectAsState()

    var dialogState: AccountEventDialog? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effects.collect {
            when (it) {
                is AccountCardEffect.NavigateBack -> onBackClick()
            }
        }
    }

    AccountCardScreenStateless(
        uiState = uiState,
        dialogState = dialogState,
        changeDialogState = { eventClicked -> dialogState = eventClicked },
        onBackClick = onBackClick,
        closeAccount = remember { { viewModel.closeAccount() } },
        moneyOperation = { amount ->
            when (dialogState) {
                is AccountEventDialog.Deposit -> viewModel.deposit(amount)
                is AccountEventDialog.Withdraw -> viewModel.withdraw(amount)
                else -> {}
            }
        }

    )
}

private sealed class AccountEventDialog {
    data object Deposit : AccountEventDialog()

    data object Withdraw : AccountEventDialog()
}

@Composable
private fun AccountCardScreenStateless(
    uiState: Account?,
    onBackClick: () -> Unit,
    closeAccount: () -> Unit = {},
    dialogState: AccountEventDialog?,
    changeDialogState: (AccountEventDialog?) -> Unit,
    moneyOperation: (Double) -> Unit = {}
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

            when (it) {
                is AccountEventDialog.Deposit -> {
                    dialogTitle = "Пополнение счёта"
                    dialogText = "Введите сумму для пополнения"
                }

                is AccountEventDialog.Withdraw -> {
                    dialogTitle = "Вывод средств"
                    dialogText = "Введите сумму для вывода"
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
//                        TextField(
//                            value = inputNumber,
//                            onValueChange = { newNumber -> inputNumber = newNumber },
//                            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
//                        )
                    }
                },
                onDismissRequest = {
                    changeDialogState(null)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            inputNumber?.let { value ->
                                moneyOperation(value)
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
                    Text(text = "id $id")
                    Text(text = "Баланс ${balance / 100f} руб.")
                    Text(text = type.toString())
                    Text(text = "Дата создания " + Date(creationDate.seconds * 1000).convertToReadableTimeLess())
                    Text(text = "Дата закрытия `" + Date(closingDate.seconds * 1000).convertToReadableTimeLess())
                    Text(text = "Счёт заблокирован  $isBlocked")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { changeDialogState(AccountEventDialog.Withdraw) }) {
                Text(text = "Вывести")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { changeDialogState(AccountEventDialog.Deposit) }) {
                Text(text = "Пополнить")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text(text = "Перевести")
            }

        }
    }
}

@Composable
@Preview
private fun AccountCardScreenPreview() {
    TrustBankClientMobileTheme {
        AccountCardScreenStateless(
            uiState = Account.newBuilder().setId("1").setBalance(1000).build(),
            onBackClick = {},
            dialogState = null,
            changeDialogState = {},
            moneyOperation = {}
        )
    }
}
