package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.*
import com.diegoferreiracaetano.dlearn.ui.screens.favorites.FavoritesScreen
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.new.NewScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.CreateNewPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.LoginScreen
import com.diegoferreiracaetano.dlearn.ui.screens.onboarding.OnboardingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.WelcomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.ResetPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.SignUpScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.VerifyAccountScreen
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailScreen
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    sessionManager: SessionManager = koinInject(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val uriHandler = LocalUriHandler.current
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()
   // val startDestination = if (isLoggedIn) Home.route else Onboarding.route

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
                onLoginClick = { navController.navigate(Home.route) },
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
                onItemClick = { id ->
                    navController.handleNavigation(id, uriHandler)
                },
                onClose = {
                    navController.popBackStack()
                },
                modifier = modifier,
            )
        }

        composable(
            route = MovieDetail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.read { getString("movieId") } ?: ""
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onItemClick = { id ->
                    navController.handleNavigation(id, uriHandler)
                },
                modifier = modifier
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

        composable(Profile.route) {
            ProfileScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                onItemClick = { id ->
                    navController.handleNavigation(id, uriHandler)
                },
                onClose = {
                    navController.popBackStack()
                },
                modifier = modifier,
            )
        }
    }
}

private fun NavHostController.handleNavigation(
    id: String, 
    uriHandler: UriHandler
) {
    if (id.startsWith("youtube://")) {
        val videoId = id.removePrefix("youtube://")
        uriHandler.openUri("https://www.youtube.com/watch?v=$videoId")
    } else if (id.startsWith("http") || id.contains("://")) {
        try {
            uriHandler.openUri(id)
        } catch (e: Exception) {
            // Silently fail if URI cannot be opened
        }
    } else {
        navigate(MovieDetail.createRoute(id))
    }
}

fun NavHostController.navigateToRoute(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}
