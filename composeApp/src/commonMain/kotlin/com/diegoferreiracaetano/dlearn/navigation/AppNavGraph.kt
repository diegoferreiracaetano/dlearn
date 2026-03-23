package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.*
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.password.CreateNewPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.VerifyAccountScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.LoginScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.ResetPasswordScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.SignUpScreen
import com.diegoferreiracaetano.dlearn.ui.screens.login.WelcomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.main.MainScreen
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailScreen
import com.diegoferreiracaetano.dlearn.ui.screens.onboarding.OnboardingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.search.SearchMainScreen
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
    // Handler especializado para eventos globais (MFA, Mensagens, Navegação)
    val eventHandler = remember(navController, snackbarHostState, coroutineScope) {
        GlobalEventHandler(navController, snackbarHostState, coroutineScope)
    }

    // Observador Central de Eventos Globais
    LaunchedEffect(eventDispatcher) {
        eventDispatcher.events.collect { event ->
            eventHandler.handle(event)
        }
    }

    val startDestination = Profile.route
    val uriHandler = LocalUriHandler.current

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
                onSignUpClick = { navController.navigate(NavigationRoutes.VERIFY_ACCOUNT) },
                modifier = modifier
            )
        }

        composable(ResetPassword.route) {
            ResetPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(NavigationRoutes.VERIFY_ACCOUNT) },
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
                onSuccess = { 
                    navController.popBackStack() 
                },
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
                modifier = Modifier
            )
        }

        composable(Search.route) {
            SearchMainScreen(
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(Home.route) {
            MainScreen(
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                onSearchClick = { navController.navigate(Search.route) },
                modifier = modifier,
                currentRoute = Home.route
            )
        }

        composable(Watchlist.route) {
            MainScreen(
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                modifier = modifier,
                currentRoute = Watchlist.route
            )
        }

        composable(Favorite.route) {
            MainScreen(
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                modifier = modifier,
                currentRoute = Favorite.route
            )
        }

        composable(Profile.route) {
            MainScreen(
                onItemClick = { route -> navController.navigateToPath(route) },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                modifier = modifier,
                currentRoute = Profile.route
            )
        }

        composable(SettingsNotifications.route) {
            SettingsScreen(
                path = NavigationRoutes.SETTINGS_NOTIFICATIONS,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(SettingsLanguage.route) {
            SettingsScreen(
                path = NavigationRoutes.SETTINGS_LANGUAGE,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(SettingsCountry.route) {
            SettingsScreen(
                path = NavigationRoutes.SETTINGS_COUNTRY,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }

        composable(
            route = NavigationRoutes.APP_ROUTE,
            arguments = listOf(
                navArgument(NavigationRoutes.PATH_ARG) { type = NavType.StringType },
                navArgument(NavigationRoutes.PARAMS_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument(NavigationRoutes.FAQ_REF_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            AppScreen(
                path = backStackEntry.sduiPath,
                params = backStackEntry.sduiParams,
                onBackClick = { navController.popBackStack() },
                onTabSelected = { route -> navController.navigateToRoute(route) },
                onItemClick = { route -> navController.navigate(route) },
                onNavigate = { route -> navController.navigate(route) },
                onDeeplink = { url -> uriHandler.openUri(url) },
                modifier = modifier
            )
        }

        composable(
            route = MovieDetail.route,
            arguments = listOf(navArgument(NavigationRoutes.MOVIE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.readOrDefault(NavigationRoutes.MOVIE_ID_ARG, "")
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = { navController.popBackStack() },
                onItemClick = { id -> navController.navigate(MovieDetail.createRoute(id)) },
                modifier = modifier
            )
        }
    }
}
