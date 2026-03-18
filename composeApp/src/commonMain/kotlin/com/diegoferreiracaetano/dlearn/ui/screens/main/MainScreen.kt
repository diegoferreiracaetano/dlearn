package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.ProvideTopBarManager
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    onSearchClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    currentRoute: String = NavigationRoutes.HOME
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(currentRoute, onTabSelected) {
        ComponentActions(
            currentRoute = currentRoute,
            onSearchClick = onSearchClick,
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }
    
    ProvideTopBarManager {
        MainContent(
            uiState = uiState,
            actions = actions,
            modifier = modifier
        )
    }
}

@Composable
fun MainContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    uiState.Render(
        actions = actions,
        modifier = modifier
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    val bottomNavItems = listOf(
        BottomNavItem(NavigationRoutes.HOME, NavigationRoutes.HOME, AppIconType.HOME),
        BottomNavItem(NavigationRoutes.WATCHLIST, NavigationRoutes.WATCHLIST, AppIconType.WATCHLIST),
        BottomNavItem(NavigationRoutes.FAVORITE, NavigationRoutes.FAVORITE, AppIconType.FAVORITE),
        BottomNavItem(NavigationRoutes.PROFILE, NavigationRoutes.PROFILE, AppIconType.PERSON)
    )

    val components = listOf(
        AppContainerComponent(
            topBar = AppTopBarComponent(title = "DLearn", showSearch = true),
            bottomBar = BottomNavigationComponent(
                items = bottomNavItems,
                selectedRoute = NavigationRoutes.HOME
            ),
            components = listOf()
        )
    )

    DLearnTheme {
         MainContent(
             uiState = UIState.Success(Screen(id = "main", components = components)),
             actions = ComponentActions()
         )
    }
}
