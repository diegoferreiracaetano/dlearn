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
import com.diegoferreiracaetano.dlearn.navigation.ScreenRouter.Home
import com.diegoferreiracaetano.dlearn.ui.views.home.HomeScreen
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
    // val startDestination = if (isLoggedIn) Home.route else Onboarding.route

    val startDestination = Home.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Home.route) {
            HomeScreen(
                onTabSelected = { route ->
                    navController.navigateToRoute(route)
                },
                onItemClick = {
                    //   navController.navigate(PokemonDetail.routeWithId(it))
                },
                modifier = modifier,
            )
        }
    }
//        composable( Onboarding.route) {
//            OnboardingScreen(
//                onOnboardingFinished = { navController.navigate(OnboardingFinish.route) },
//                modifier = modifier
//            )
//        }
//
//        composable( OnboardingFinish.route) {
//            OnboardingFinishScreen(
//                onCreateAccount = {
//                    navController.navigate(PreLogin.routeWithType(SIGN_UP))
//                },
//                onLogin = {
//                    navController.navigate(PreLogin.routeWithType(LOGIN))
//                },
//                onSkip = {},
//                modifier = modifier
//            )
//        }
//
//        composable(PreLogin.route) { backStackEntry ->
//            val type = backStackEntry.readEnumOrDefault(TYPE_ARG, AuthScreenType.entries.first())
//            PreLoginScreen(
//                type = type,
//                onBack = {
//                    navController.popBackStack()
//                },
//                onAccountCreated = {
//                    when (type) {
//                        SIGN_UP -> navController.navigate(CreateAccount.route)
//                        LOGIN -> navController.navigate(Login.route)
//                    }
//                },
//                modifier = modifier
//            )
//        }
//
//        composable(CreateAccount.route) { backStackEntry ->
//            val step = backStackEntry.readEnumOrDefault(STEP_ARG, CreateAccountStepType.entries.first())
//            CreateAccountScreen(
//                step = step,
//                onNext = { nextStep ->
//                    navController.navigate(CreateAccount.routeWithStep(nextStep))
//                },
//                onFinish = { navController.navigateClearBackStackTo(Home.route) },
//                onBack = { navController.popBackStack() },
//                modifier = modifier
//            )
//        }
//
//        composable(Login.route) {
//            LoginScreen(
//                onFinish = { navController.navigateClearBackStackTo(Home.route) },
//                onBack = { navController.popBackStack() },
//                onChangePassword = {
//                    navController.navigate(ValidateEmail.routeWithType(FORGOT))
//                },
//                modifier = modifier
//            )
//        }
//
//        composable(ValidateEmail.route) { backStackEntry ->
//            val type = backStackEntry.readEnumOrDefault(VALIDATE_ARG, ValidateEmailType.entries.first())
//            ValidateEmailScreen(
//                type = type,
//                onSendCode = { contact->
//                    navController.navigate(SendCode.routeWithContact(contact))
//                },
//                onBack = { navController.popBackStack() },
//                modifier = modifier
//            )
//        }
//
//        composable(SendCode.route) {  backStackEntry ->
//            val contact = backStackEntry.readOrDefault(CONTACT_ARG, "")
//            SendCodeScreen(
//                contact = contact,
//                onFinish = { navController.navigateClearBackStackTo(Login.route) },
//                onBack = { navController.popBackStack() },
//                modifier = modifier
//            )
//        }
//
//        composable(Profile.route) {
//
//            ProfileScreen(
//                isLoggedIn = isLoggedIn,
//                name = sessionManager.user()?.name.orEmpty(),
//                email = sessionManager.user()?.email.orEmpty(),
//                showSuccess = false,
//                onLoginClick = {
//                    navController.navigate(PreLogin.routeWithType(LOGIN))
//                },
//                onNameClick = {
//                    navController.navigate(EditName.route)
//                },
//                onEmailClick = {
//                    navController.navigate(ValidateEmail.routeWithType(UPDATE))
//                },
//                onPasswordClick = {
//                    navController.navigate(EditPassword.route)
//                },
//                onTabSelected = { route->
//                    navController.navigate(route)
//                },
//                onLogoutClick = {
//                    coroutineScope.launch{
//                        sessionManager.logout()
//                    }
//                },
//                modifier = modifier
//            )
//        }
//
//        composable(EditName.route) {
//            ChangeUserNameScreen(
//                onBack = { navController.popBackStack() },
//                onFinish = { navController.navigateClearBackStackTo(Profile.route) },
//                modifier = modifier
//            )
//        }
//
//        composable(EditPassword.route) {
//            ChangePasswordScreen(
//                onBack = { navController.popBackStack() },
//                onFinish = { navController.navigateClearBackStackTo(Profile.route) },
//                modifier = modifier
//            )
//        }
//
//        composable(Regions.route) {
//            RegionsScreen(
//                onTabSelected = { route->
//                    navController.navigate(route)
//                },
//                modifier
//            )
//        }
//
//        composable(Favorites.route) {
//            FavoritesScreen(
//                onTabSelected = { route->
//                    navController.navigate(route)
//                },
//                modifier
//            )
//        }
//
//        composable(PokemonDetail.route) { backStackEntry ->
//            val id = backStackEntry.readOrDefault(DETAIL_ARG, "")
//
//            PokemonDetailScreen(
//                id = id,
//                onBack = { navController.popBackStack() },
//                modifier = modifier
//            )
//        }
//    }
}
