package com.trustbank.client_mobile.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trustbank.client_mobile.presentation.authorization.login.LoginScreen
import com.trustbank.client_mobile.presentation.authorization.register.RegisterScreen
import com.trustbank.client_mobile.presentation.main.MainScreen

sealed class AppNavigation : Navigation() {
    data object Login : AppNavigation() {
        override val route: String = "login"
    }

    data object Register : AppNavigation() {
        override val route: String = "register"
    }

    data object Main : AppNavigation() {
        override val route: String = "main"
    }


}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppNavigation.Login.route,
        modifier = modifier
    ) {
        composable(
            route = AppNavigation.Login.route,
        ) {
            LoginScreen(
                navController = navController
            )
        }

        composable(
            route = AppNavigation.Register.route,
        ) {
            RegisterScreen(
                navController = navController
            )
        }
        composable(
            route = AppNavigation.Main.route
        ) {
            MainScreen()
        }
    }
}

/**
 * Shows transition animation after navigating to screen.
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )
}

/**
 * Shows transition animation after navigating from screen.
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )
}