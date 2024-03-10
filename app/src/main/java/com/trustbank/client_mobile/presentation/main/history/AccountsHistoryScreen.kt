package com.trustbank.client_mobile.presentation.main.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.main.accounts.list.AccountItem
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.proto.Account
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsHistoryScreen(
    navigateBack: () -> Unit,
    navigateToAccountHistory: (String) -> Unit
) {
    val viewModel: AccountsHistoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (pullRefreshState.isRefreshing) {
            viewModel.refreshTransactionList()
            delay(1000L)
            pullRefreshState.endRefresh()
        }
    }

    AccountsHistoryScreenStateless(
        pullRefreshState = pullRefreshState,
        uiState = uiState,
        onBackClick = navigateBack,
        onAccountClick = navigateToAccountHistory
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountsHistoryScreenStateless(
    pullRefreshState: PullToRefreshState,
    uiState: List<Account>,
    onBackClick: () -> Unit,
    onAccountClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SingleLevelAppBar(
                title = "История счетов",
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
                items(uiState) { account ->
                    Spacer(modifier = Modifier.padding(top = PADDING_BIG))
                    AccountItem(
                        account = account,
                        onClick = onAccountClick
                    )
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )
        }

    }
}



