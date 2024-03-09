package com.trustbank.client_mobile.presentation.main.accounts.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.Loan
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountsListScreen(
    navController: NavHostController,
    navigateToAccountCard: (String) -> Unit
) {
    val viewModel: UserAccountsListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (pullRefreshState.isRefreshing) {
            viewModel.refreshAccountsList()
            delay(1000L)
            pullRefreshState.endRefresh()
        }
    }



    UserInfoScreenStateless(
        pullRefreshState = pullRefreshState,
        uiState = uiState,
        navigateToAccountCard = navigateToAccountCard,
        openNewAccount = remember { { viewModel.openNewAccount() } }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UserInfoScreenStateless(
    pullRefreshState: PullToRefreshState,
    uiState: UserInfoUiState,
    navigateToAccountCard: (id: String) -> Unit = {},
    openNewAccount: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Дебетовые", "Кредитные")

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SingleLevelAppBar(
                title = "Счета",
                backButtonRequired = false
            )
        },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                IconButton(onClick = { openNewAccount() }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(
                                    tabPositions[selectedTabIndex]
                                )
                            )
                        }
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> DebitAccountsList(
                        accounts = uiState.accounts,
                        navigateToAccountCard = navigateToAccountCard
                    )

                    1 -> CreditAccountsList(
                        accounts = uiState.loans,
                        navigateToAccountCard = navigateToAccountCard
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


@Composable
fun DebitAccountsList(
    accounts: List<Account>,
    navigateToAccountCard: (id: String) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(accounts) {
            ListItem(
                { Text(text = "Счёт id = ${it.id}\nБаланс: ${it.balance / 100f} руб.") },
                modifier = Modifier.clickable { navigateToAccountCard(it.id) }
            )
        }
    }
}

@Composable
fun CreditAccountsList(
    accounts: List<Loan>,
    navigateToAccountCard: (id: String) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(accounts) {
            ListItem(
                { Text(text = "Счёт id = ${it.id}\nЗадолжность: ${it.amountDebt / 100f} руб.") },
                modifier = Modifier.clickable { navigateToAccountCard(it.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun UserInfoScreenPreview() {
    TrustBankClientMobileTheme {
        UserInfoScreenStateless(
            pullRefreshState = rememberPullToRefreshState(),
            uiState = UserInfoUiState(
                accounts = listOf(
                    Account.newBuilder().setId("1").setBalance(1000).build(),
                    Account.newBuilder().setId("2").setBalance(2000).build(),
                    Account.newBuilder().setId("3").setBalance(3000).build(),
                )
            )
        )
    }
}