package com.diegoferreiracaetano.dlearn.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.home.LoadingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.profile.state.ProfileUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ProfileUiState.Success -> {
            ProfileListScreen(
                screen = state.screen,
                onTabSelected = onTabSelected,
                onItemClick = onItemClick,
                modifier = modifier,
            )
        }
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Error -> {
            AppError(
                throwable = state.throwable,
                modifier = modifier,
                onPrimary = viewModel::retry,
                onClose = onClose
            )
        }
    }
}

@Composable
fun ProfileListScreen(
    screen: Screen,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = remember(onItemClick, onTabSelected) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected
        )
    }

    screen.components.forEach { component ->
        RenderComponentFactory.Render(
            modifier = modifier,
            component = component,
            actions = actions
        )
    }
}
