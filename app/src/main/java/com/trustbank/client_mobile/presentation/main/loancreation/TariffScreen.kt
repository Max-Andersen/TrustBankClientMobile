package com.trustbank.client_mobile.presentation.main.loancreation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.sibstream.digitallab.ui.topbar.SingleLevelAppBar
import com.trustbank.client_mobile.presentation.common.EmptyList
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_BIG
import com.trustbank.client_mobile.presentation.ui.theme.PADDING_MEDIUM
import com.trustbank.client_mobile.proto.LoanRequest
import com.trustbank.client_mobile.proto.LoanTariff
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCreationScreen(
    onTariffClick: (id: String, rate: String) -> Unit,
    onLoanRequestClick: (String) -> Unit = {}
) {
    val viewModel: TariffListViewModel = koinViewModel()
    val tariffs by viewModel.tariffList.collectAsState()
    val loanRequests by viewModel.loanRequestsList.collectAsState()


    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (pullRefreshState.isRefreshing) {
            viewModel.refreshData()
            delay(1000L)
            pullRefreshState.endRefresh()
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Тарифы", "Заявки на кредит")

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SingleLevelAppBar(
                title = "Оформление кредите",
                backButtonRequired = false
            )
        },

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
                    0 -> TariffGridContent(tariffs, onTariffClick)

                    1 -> LoanRequests(loanRequests, onLoanRequestClick)
                }

            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )

        }
    }


//    TariffListScreenContent(
//        items = tariffs,
//        refreshTariffs = viewModel::refreshTariffs,
//        onTariffClick = onTariffClick
//    )
}


@Composable
private fun TariffGridContent(
    items: List<LoanTariff>,
    onTariffClick: (String, String) -> Unit
) {
    if (items.isEmpty()) {
        Box {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                EmptyList()
            }
        }
    } else {
        val listState = rememberLazyGridState()
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = spacedBy(8.dp),
                horizontalArrangement = spacedBy(8.dp),
            ) {
                items(items = items, key = { it.id }) {
                    TariffListItem(
                        item = it,
                        onClick = { onTariffClick(it.id, it.interestRate.toString()) })
                }
            }
        }
    }
}


@Composable
private fun LoanRequests(
    items: List<LoanRequest>,
    onLoanRequestClick: (String) -> Unit
) {
    if (items.isEmpty()) {
        Box {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                EmptyList()
            }
        }
    } else {
        val listState = rememberLazyListState()
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = PADDING_BIG)
                    .padding(horizontal = PADDING_BIG),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM)
            ) {
                items(items) {
                    LoanRequestListItem(it) {
                        onLoanRequestClick(it.id)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TariffListScreenContent(
    items: List<LoanTariff>?,
    refreshTariffs: suspend () -> Unit = {},
    onTariffClick: (String, String) -> Unit
) {
    if (items == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (items.isEmpty()) {
        val refreshState = rememberPullToRefreshState()
        if (refreshState.isRefreshing) {
            LaunchedEffect(true) {
                refreshTariffs()
                delay(500L)
                refreshState.endRefresh()
            }
        }
        Box(Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                EmptyList()
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    } else {
        val listState = rememberLazyGridState()
        val refreshState = rememberPullToRefreshState()
        if (refreshState.isRefreshing) {
            LaunchedEffect(true) {
                refreshTariffs()
                delay(500L)
                listState.animateScrollToItem(0)
                delay(500L)
                refreshState.endRefresh()
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .nestedScroll(refreshState.nestedScrollConnection)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = spacedBy(8.dp),
                horizontalArrangement = spacedBy(8.dp),
            ) {
                items(items = items, key = { it.id }) {
                    TariffListItem(
                        item = it,
                        onClick = { onTariffClick(it.id, it.interestRate.toString()) })
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}