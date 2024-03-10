package com.trustbank.client_mobile.presentation.main.history.operations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.main.accounts.list.AccountItem
import com.trustbank.client_mobile.presentation.main.history.AccountsHistoryViewModel
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.utils.SpacerMedium
import com.trustbank.client_mobile.presentation.ui.utils.SpacerSmall
import com.trustbank.client_mobile.presentation.ui.utils.convertToReadableTimeLess
import com.trustbank.client_mobile.proto.Transaction
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOperationsScreen() {
    val viewModel: AccountOperationsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (pullRefreshState.isRefreshing) {
            viewModel.refreshAccountsList()
            delay(1000L)
            pullRefreshState.endRefresh()
        }
    }


    AccountOperationsScreenStateless(
        pullRefreshState = pullRefreshState,
        transactions = uiState,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOperationsScreenStateless(
    pullRefreshState: PullToRefreshState,
    transactions: List<Transaction>
){
    Scaffold(
        topBar = {
            SingleLevelAppBar(
                title = "Операции счета",
                backButtonRequired = false
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = PADDING_BIG)
                    .padding(horizontal = PADDING_BIG),
                verticalArrangement = Arrangement.spacedBy(PADDING_BIG)
            ) {
                items(transactions) { transaction ->
                    OutlinedCard{
                        Column(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            with(transaction){
                                Text(
                                    text = "id = ${transaction.id}",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Сумма: ${amount / 100f} руб.",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Дата: ${Date(date.seconds).convertToReadableTimeLess()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Тип: ${type}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Статус: ${state}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Отправитель: ${payer.ownerFullName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                SpacerSmall()
                                Text(
                                    text = "Получатель: ${payee.ownerFullName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )
        }

    }
}