package com.trustbank.client_mobile.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen(route = "login")

    data object Register : Screen(route = "register")

    data object Main : Screen(route = "main")
}