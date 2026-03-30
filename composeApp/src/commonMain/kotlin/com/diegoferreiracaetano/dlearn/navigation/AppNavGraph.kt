package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.*
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.password.CreateNewPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.VerifyAccountScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.LoginScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.ResetPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.SignUpScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.WelcomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.logout.LogoutScreen
import com.diegoferreiracaetano.dlearn.ui.screens.main.MainScreen
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailScreen
import com.diegoferreiracaetano.dlearn.ui.screens.onboarding.OnboardingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.settings.CleanCacheScreen
import com.diegoferreiracaetano.dlearn.ui.screens.settings.SettingsScreen
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    sessionManager: SessionManager = koinInject(),
    eventDispatcher: GlobalEventDispatcher = koinInject(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current
) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState()

    val eventHandler = remember(navController, snackbarHostState, coroutineScope) {
        GlobalEventHandler(navController, snackbarHostState, coroutineScope)
    }

    LaunchedEffect(eventDispatcher, eventHandler) {
        eventDispatcher.events.collect { event ->
            eventHandler.handle(event)
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Welcome.route) {
                popUpTo(0)
            }
        }
    }

    val startDestination = if (isLoggedIn) Home.route else Welcome.route

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
                onNavigateToHome = { navController.navigateToRoute(Home.route) },
                modifier = modifier
            )
        }

        composable(Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = { navController.navigateToRoute(Home.route) },
                onForgotPasswordClick = { navController.navigate(ResetPassword.route) },
                modifier = modifier
            )
        }

        composable(SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = { navController.navigateToRoute(Home.route) },
                modifier = modifier
            )
        }

        composable(ResetPassword.route) {
            ResetPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(AppNavigationRoute.VERIFY_ACCOUNT) },
                modifier = modifier
            )
        }

        composable(CreateNewPassword.route) {
            CreateNewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(ChangePassword.route) {
            CreateNewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
                modifier = modifier
            )
        }
        
        dialog(
            route = VerifyAccount.route,
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            VerifyAccountScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(Home.route) {
            MainScreen(
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                onItemClick = { route -> navController.navigateToPath(route) },
                onSearchClick = { navController.navigateToPath(Search.route) },
                modifier = modifier,
                currentRoute = Home.route
            )
        }

        composable(Watchlist.route) {
            MainScreen(
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onItemClick = { route -> navController.navigateToPath(route) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                modifier = modifier,
                currentRoute = Watchlist.route
            )
        }

        composable(Favorite.route) {
            MainScreen(
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onItemClick = { route -> navController.navigateToPath(route) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                modifier = modifier,
                currentRoute = Favorite.route
            )
        }

        composable(Profile.route) {
            MainScreen(
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onItemClick = { route -> navController.navigateToPath(route) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                onClose = { navController.navigate(Logout.route) },
                modifier = modifier,
                currentRoute = Profile.route
            )
        }

        composable(SettingsNotifications.route) {
            SettingsScreen(
                path = AppNavigationRoute.SETTINGS_NOTIFICATIONS,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(SettingsLanguage.route) {
            SettingsScreen(
                path = AppNavigationRoute.SETTINGS_LANGUAGE,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(SettingsCountry.route) {
            SettingsScreen(
                path = AppNavigationRoute.SETTINGS_COUNTRY,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        dialog(SettingsClearCache.route) {
            CleanCacheScreen(
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        dialog(Logout.route) {
            LogoutScreen(
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(
            route = AppNavigationRoute.SDUI_APP_ROUTE,
            arguments = listOf(
                navArgument(AppNavigationRoute.ARG_PATH) { type = NavType.StringType },
                navArgument(AppNavigationRoute.ARG_PARAMS) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(AppQueryParam.REF) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            AppScreen(
                path = backStackEntry.sduiPath,
                params = backStackEntry.sduiParams,
                onClose = { navController.navigate(Logout.route) },
                onBackClick = { navController.popBackStack() },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onItemClick = { route -> navController.navigateToPath(route) },
                modifier = modifier
            )
        }

        composable(
            route = MovieDetail.route,
            arguments = listOf(navArgument(AppNavigationRoute.ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.readOrDefault(AppNavigationRoute.ARG_ID, "")
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onMovieClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                modifier = modifier
            )
        }
    }
}
