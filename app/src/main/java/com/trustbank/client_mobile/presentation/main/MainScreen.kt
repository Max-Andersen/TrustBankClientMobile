package com.trustbank.client_mobile.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trustbank.client_mobile.presentation.main.accounts.list.UserAccountsListScreen
import com.trustbank.client_mobile.presentation.navigation.AppNavigation
import com.trustbank.client_mobile.presentation.navigation.BottomBarScreen

@Composable
fun MainScreen(
    externalNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) },
    )
    { paddings ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(paddingValues = paddings)
        ) {
            composable(BottomBarScreen.Home.route) {
                UserAccountsListScreen(navController = navController) {
                    externalNavController.navigate(AppNavigation.AccountCard.routeTo(it))
                }
            }
            composable(BottomBarScreen.Loans.route) {
                TestScreen(name = "Loans")
            }
            composable(BottomBarScreen.History.route) {
                TestScreen(name = "History")
            }
        }
    }
}


@Composable
fun TestScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}


@Composable
fun AppBottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Loans,
        BottomBarScreen.Home,
        BottomBarScreen.History
    )
    var navigationSelectedItem by remember {
        mutableIntStateOf(1)
    }
    NavigationBar {
        screens.forEachIndexed { index, bottomBarScreen ->
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                onClick = {
                    navigationSelectedItem = index
                    navController.navigate(bottomBarScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = bottomBarScreen.icon),
                        contentDescription = null
                    )
                }
            )
        }
    }
}