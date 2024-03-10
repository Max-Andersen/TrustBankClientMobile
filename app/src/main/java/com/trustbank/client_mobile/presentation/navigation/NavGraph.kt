package com.trustbank.client_mobile.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.trustbank.client_mobile.presentation.authorization.login.LoginScreen
import com.trustbank.client_mobile.presentation.authorization.register.RegisterScreen
import com.trustbank.client_mobile.presentation.main.MainScreen
import com.trustbank.client_mobile.presentation.main.accounts.card.AccountCardScreen
import com.trustbank.client_mobile.presentation.main.accounts.card.CreditCardScreen
import com.trustbank.client_mobile.presentation.main.newcredit.CreateCreditScreen

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

    data object AccountCard : AppNavigation() {
        override val route: String = "accountCard"
        override val arguments: List<NamedNavArgument> = generateMaskFromArguments {
            listOf(navArgument("id") { type = NavType.StringType })
        }


        fun routeTo(id: String): String = super.routeTo(id)
    }

    data object CreditCard : AppNavigation() {
        override val route: String = "creditCard"
        override val arguments: List<NamedNavArgument> = generateMaskFromArguments {
            listOf(navArgument("id") { type = NavType.StringType })
        }


        fun routeTo(id: String): String = super.routeTo(id)
    }

    data object NewCreditCard : AppNavigation() {
        override val route: String = "newCreditCard"
        override val arguments: List<NamedNavArgument> = generateMaskFromArguments {
            listOf(
                navArgument("tariffId") { type = NavType.StringType },
                navArgument("interestRate") { type = NavType.StringType },
            )
        }


        fun routeTo(id: String, rate: String): String = super.routeTo(id, rate)
    }

    data object NewDebitAccount : AppNavigation() {
        override val route: String = "newDebitAccount"
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
            MainScreen(navController)
        }



        composable(
            route = AppNavigation.NewCreditCard.mask,
            arguments = AppNavigation.NewCreditCard.arguments

        ) {
            val tariffId = it.arguments?.getString("tariffId")
            val interestRate = it.arguments?.getString("interestRate") ?: ""

            CreateCreditScreen(
                interestRatio = interestRate,
                mainScreenNavigate = {
                    navController.navigate(AppNavigation.Main.route)
                })
        }

        composable(
            route = AppNavigation.AccountCard.mask,
            arguments = AppNavigation.AccountCard.arguments
        ) {
            AccountCardScreen(navController = navController, onBackClick = {
                navController.popBackStack()
            })
        }

        composable(
            route = AppNavigation.CreditCard.mask,
            arguments = AppNavigation.CreditCard.arguments
        ) {
            CreditCardScreen {
                navController.popBackStack()
            }
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