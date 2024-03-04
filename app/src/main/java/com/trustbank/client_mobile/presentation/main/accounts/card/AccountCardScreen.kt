package com.trustbank.client_mobile.presentation.main.accounts.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
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

    LaunchedEffect(key1 = Unit){
        viewModel.effects.collect{
            when(it){
                is AccountCardEffect.NavigateBack -> onBackClick()
            }
        }
    }

    AccountCardScreenStateless(
        uiState = uiState,
        onBackClick = onBackClick,
        closeAccount = remember{{ viewModel.closeAccount() }}
    )

}

@Composable
fun AccountCardScreenStateless(
    uiState: Account?,
    onBackClick: () -> Unit,
    closeAccount: () -> Unit = {},
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
            Button(modifier = Modifier.fillMaxWidth() ,onClick = { /*TODO*/ }) {
                Text(text = "Вывести")
            }
            Button(modifier = Modifier.fillMaxWidth() ,onClick = { /*TODO*/ }) {
                Text(text = "Пополнить")
            }
            Button(modifier = Modifier.fillMaxWidth() ,onClick = { /*TODO*/ }) {
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
            onBackClick = {}
        )
    }
}
