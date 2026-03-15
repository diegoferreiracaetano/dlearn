package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.favorite.FavoriteScreen
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.main.state.MainUiState
import com.diegoferreiracaetano.dlearn.ui.screens.new.NewScreen
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileScreen
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentRoute by viewModel.currentRoute.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isSearchVisible by viewModel.isSearchVisible.collectAsStateWithLifecycle()

    val actions = remember(currentRoute, isSearchVisible, searchText, uiState) {
        val state = uiState
        ComponentActions(
            currentRoute = currentRoute,
            searchText = searchText,
            isSearchVisible = isSearchVisible,
            onItemClick = onItemClick,
            onClose = onClose,
            onTabSelected = viewModel::onTabSelected,
            onSearchTextChange = viewModel::onSearchTextChanged,
            onShowSearchChanged = viewModel::onShowSearchChanged,
            onRetry = viewModel::retry,
            isLoading = state is MainUiState.Loading,
            error = (state as? MainUiState.Error)?.throwable
        )
    }

    val components = when (val state = uiState) {
        is MainUiState.Success -> state.screen.components
        else -> listOf(AppContainerComponent())
    }

    components.forEach { component ->
        RenderComponentFactory.Render(
            component = component,
            actions = actions,
            modifier = modifier
        )
    }
}

@Composable
fun MainContent(
    route: String,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (route) {
        NavigationRoutes.HOME -> HomeScreen(
            onTabSelected = onTabSelected,
            onItemClick = onItemClick,
            modifier = modifier
        )

        NavigationRoutes.PROFILE -> ProfileScreen(
            onTabSelected = onTabSelected,
            onItemClick = onItemClick,
            modifier = modifier
        )

        NavigationRoutes.NEW -> NewScreen(
            onTabSelected = onTabSelected,
            onItemClick = onItemClick,
            modifier = modifier
        )

        NavigationRoutes.FAVORITE -> FavoriteScreen(
            onTabSelected = onTabSelected,
            onItemClick = onItemClick,
            modifier = modifier
        )
    }
}
