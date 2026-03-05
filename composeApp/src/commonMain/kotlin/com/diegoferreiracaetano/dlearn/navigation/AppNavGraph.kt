package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.*
import com.diegoferreiracaetano.dlearn.ui.screens.account.AccountScreen
import com.diegoferreiracaetano.dlearn.ui.screens.favorites.FavoritesScreen
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.new.NewScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.CreateNewPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.LoginScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.OnboardingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.WelcomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.ResetPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.SignUpScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.VerifyAccountScreen
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    sessionManager: SessionManager = koinInject(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()
    val startDestination = if (isLoggedIn) Home.route else Onboarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Onboarding.route) {
            OnboardingScreen(
                onFinish = { navController.navigate(Welcome.route) },
                modifier = modifier
            )
        }

        composable(Welcome.route) {
            WelcomeScreen(
                onSignUpClick = { navController.navigate(SignUp.route) },
                onLoginClick = { navController.navigate(Login.route) },
                modifier = modifier
            )
        }

        composable(Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigate(Home.route) },
                onForgotPasswordClick = { navController.navigate(ResetPassword.route) },
                modifier = modifier
            )
        }

        composable(SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onSignUpClick = { navController.navigate(VerifyAccount.route) },
                modifier = modifier
            )
        }

        composable(ResetPassword.route) {
            ResetPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(VerifyAccount.route) },
                modifier = modifier
            )
        }

        composable(VerifyAccount.route) {
            VerifyAccountScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(CreateNewPassword.route) },
                onResendClick = { /* Handle resend */ },
                modifier = modifier
            )
        }

        composable(CreateNewPassword.route) {
            CreateNewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onResetClick = { navController.navigate(Login.route) },
                modifier = modifier
            )
        }

        composable(Home.route) {
            HomeScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                onItemClick = {
                    // Implementar detalhe futuramente
                },
                modifier = modifier,
            )
        }

        composable(New.route) {
            NewScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
            )
        }

        composable(Favorites.route) {
            FavoritesScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
            )
        }

        composable(Account.route) {
            AccountScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
            )
        }
    }
}
