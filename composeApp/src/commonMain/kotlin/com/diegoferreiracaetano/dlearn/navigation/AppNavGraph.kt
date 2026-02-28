package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject

@Composable
fun AppNavGraph(
    sessionManager: SessionManager = koinInject(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
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
