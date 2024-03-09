package com.trustbank.client_mobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ShoppingCart
import com.trustbank.client_mobile.R

sealed class BottomBarScreen(
    val route: String,
    val icon: Int
) {
    data object Loans : BottomBarScreen(
        route = "loans",
        icon = R.drawable.money_icon
    )

    data object Home : BottomBarScreen(
        route = "home",
        icon = R.drawable.bank_icon
    )

    data object History : BottomBarScreen(
        route = "history",
        icon = R.drawable.shopping_cart
    )
}