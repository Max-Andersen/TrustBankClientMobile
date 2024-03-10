package com.trustbank.client_mobile.presentation.main.accounts.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_SMALL
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import com.trustbank.client_mobile.presentation.ui.utils.SpacerMedium
import com.trustbank.client_mobile.presentation.ui.utils.SpacerSmall
import com.trustbank.client_mobile.presentation.ui.utils.convertToReadableTimeLess
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.Loan
import com.trustbank.client_mobile.proto.ShortLoanInfo
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountsListScreen(
    navController: NavHostController,
    navigateToAccountCard: (String) -> Unit,
    navigateToCreditCard: (String) -> Unit
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
        navigateToCreditCard = navigateToCreditCard,
        openNewAccount = remember { { viewModel.openNewAccount() } }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UserInfoScreenStateless(
    pullRefreshState: PullToRefreshState,
    uiState: UserInfoUiState,
    navigateToAccountCard: (id: String) -> Unit = {},
    navigateToCreditCard: (id: String) -> Unit = {},
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
                OutlinedCard(
                    modifier = Modifier.size(PADDING_BIG * 3),
                    onClick = { openNewAccount() },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                PADDING_SMALL
                            )
                    )
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
                        navigateToCreditCard = navigateToCreditCard
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
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = PADDING_BIG)
            .padding(horizontal = PADDING_BIG),
        verticalArrangement = Arrangement.spacedBy(PADDING_BIG)
    ) {
        items(accounts) {
            AccountItem(
                onClick = navigateToAccountCard,
                account = it
            )
        }
    }
}

@Composable
fun AccountItem(
    onClick: (id: String) -> Unit,
    account: Account
){
    OutlinedCard(
        onClick = { onClick(account.id) }
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "id = ${account.id}",
                style = MaterialTheme.typography.bodySmall,
            )
            SpacerMedium()
            Text(text = "Баланс: ${account.balance / 100f} руб.")
        }
    }
}


@Composable
fun CreditAccountsList(
    accounts: List<ShortLoanInfo>,
    navigateToCreditCard: (id: String) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = PADDING_BIG)
            .padding(horizontal = PADDING_BIG),
        verticalArrangement = Arrangement.spacedBy(PADDING_BIG)
    ) {
        items(accounts) {
            OutlinedCard(
                onClick = { navigateToCreditCard(it.id) }
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Задолжность: ${it.amountDebt / 100f} руб.")
                    SpacerSmall()
                    Text(text = "Ставка: ${it.interestRate}%/день")
                    SpacerSmall()
                    Text("Дата открытия  ${Date(it.issuedDate.seconds).convertToReadableTimeLess()}")
                    SpacerSmall()
                    Text("Предварительная дата закрытия ${Date(it.repaymentDate.seconds).convertToReadableTimeLess()}")
                }
            }
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