package com.trustbank.client_mobile.presentation.main.tariffs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.trustbank.client_mobile.presentation.common.EmptyList
import com.trustbank.client_mobile.proto.LoanTariff
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun TariffListScreen(
    onTariffClick: (id: String, rate: String) -> Unit,
) {
    val viewModel: TariffListViewModel = koinViewModel()
    val items by viewModel.tariffList.collectAsState()


    TariffListScreenContent(
        items = items,
        refreshTariffs = viewModel::refreshTariffs,
        onTariffClick = onTariffClick
    )
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
                    TariffListItem(item = it, onClick = { onTariffClick(it.id, it.interestRate.toString()) })
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}