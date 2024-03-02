package com.trustbank.client_mobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector
) {
    data object Loans : BottomBarScreen(
        route = "loans",
        icon = Icons.Rounded.Warning
    )

    data object Home : BottomBarScreen(
        route = "home",
        icon = Icons.Rounded.Home
    )

    data object History : BottomBarScreen(
        route = "history",
        icon = Icons.Rounded.ShoppingCart
    )
}