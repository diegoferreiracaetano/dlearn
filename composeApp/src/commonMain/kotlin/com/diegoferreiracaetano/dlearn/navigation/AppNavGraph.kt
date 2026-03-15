package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.CreateNewPassword
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Favorites
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Home
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Login
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.MovieDetail
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.New
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Onboarding
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Profile
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.ResetPassword
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.SignUp
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.VerifyAccount
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Welcome
import com.diegoferreiracaetano.dlearn.ui.screens.login.CreateNewPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.LoginScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.ResetPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.SignUpScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.VerifyAccountScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.WelcomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.main.MainScreen
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailScreen
import com.diegoferreiracaetano.dlearn.ui.screens.onboarding.OnboardingScreen
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
    // val startDestination = if (isLoggedIn) Main.route else Onboarding.route

    val startDestination = Home.route

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
                onLoginClick = { navController.navigateToRoute(Home.route) },
                modifier = modifier
            )
        }

        composable(Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginClick = { navController.navigateToRoute(Home.route) },
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
            MainScreen(
                onItemClick = { id ->
                    navController.navigate(MovieDetail.createRoute(id))
                },
                onClose = {
                    navController.popBackStack()
                },
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
                currentRoute = Home.route
            )
        }

        composable(New.route) {
            MainScreen(
                onItemClick = { id ->
                    navController.navigate(MovieDetail.createRoute(id))
                },
                onClose = {
                    navController.popBackStack()
                },
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
                currentRoute = New.route
            )
        }

        composable(Favorites.route) {
            MainScreen(
                onItemClick = { id ->
                    navController.navigate(MovieDetail.createRoute(id))
                },
                onClose = {
                    navController.popBackStack()
                },
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
                currentRoute = Favorites.route
            )
        }

        composable(Profile.route) {
            MainScreen(
                onItemClick = { id ->
                    navController.navigate(MovieDetail.createRoute(id))
                },
                onClose = {
                    navController.popBackStack()
                },
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                modifier = modifier,
                currentRoute = Profile.route
            )
        }

        composable(
            route = MovieDetail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.readOrDefault("movieId", "")
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onItemClick = { id ->
                    navController.navigate(MovieDetail.createRoute(id))
                },
                modifier = modifier
            )
        }
    }
}
