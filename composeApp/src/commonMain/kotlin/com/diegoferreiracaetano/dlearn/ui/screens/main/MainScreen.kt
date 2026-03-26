package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import com.diegoferreiracaetano.dlearn.ui.viewmodel.main.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    onClose: () -> Unit = {},
    onSearchClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
    currentRoute: String = AppNavigationRoute.HOME
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(currentRoute, onTabSelected, onClose) {
        ComponentActions(
            currentRoute = currentRoute,
            onSearchClick = onSearchClick,
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onClose = onClose,
            onRetry = viewModel::retry
        )
    }
    MainContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )

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
        BottomNavItem(AppNavigationRoute.HOME, AppNavigationRoute.HOME, AppIconType.HOME),
        BottomNavItem(AppNavigationRoute.WATCHLIST, AppNavigationRoute.WATCHLIST, AppIconType.WATCHLIST),
        BottomNavItem(AppNavigationRoute.FAVORITE, AppNavigationRoute.FAVORITE, AppIconType.FAVORITE),
        BottomNavItem(AppNavigationRoute.PROFILE, AppNavigationRoute.PROFILE, AppIconType.PERSON)
    )

    val selectedRoute = AppNavigationRoute.HOME

    val components = listOf(
        AppContainerComponent(
            topBar = AppTopBarListComponent(
                listOf(
                    AppTopBarItem(
                        AppTopBarComponent(title = "DLearn", showSearch = true),
                        selectedRoute)
                ),
                selectedActionUrl =selectedRoute
            ),
            bottomBar = BottomNavigationComponent(
                items = bottomNavItems,
                selectedActionUrl = selectedRoute
            ),
            components = listOf()
        )
    )

    DLearnTheme {
        MainContent(
            uiState = UIState.Success(Screen(components = components)),
            actions = ComponentActions()
        )
    }
}
