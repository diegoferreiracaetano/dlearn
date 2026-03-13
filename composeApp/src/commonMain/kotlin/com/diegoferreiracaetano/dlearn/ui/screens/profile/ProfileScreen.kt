package com.diegoferreiracaetano.dlearn.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.profile.state.ProfileUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview
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

    ProfileScreenContent(
        uiState = uiState,
        onTabSelected = onTabSelected,
        onItemClick = onItemClick,
        onClose = onClose,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileUiState,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is ProfileUiState.Success -> {
            ProfileListScreen(
                screen = uiState.screen,
                onTabSelected = onTabSelected,
                onItemClick = onItemClick,
                modifier = modifier,
            )
        }

        is ProfileUiState.Loading -> AppLoading()
        is ProfileUiState.Error -> {
            AppError(
                throwable = uiState.throwable,
                modifier = modifier,
                onPrimary = onRetry,
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

@Preview
@Composable
fun ProfileListScreenPreview() {
    val screen = Screen(
        id = "profile",
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(title = "Profile"),
                components = listOf(
                    ProfileRowComponent(
                        name = "Diego Ferreira",
                        email = "diego@example.com",
                        imageUrl = null
                    ),
                    FooterComponent(label = "Sair")
                )
            )
        )
    )

    DLearnTheme {
        ProfileListScreen(
            screen = screen,
            onTabSelected = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
fun ProfileScreenContentSuccessPreview() {
    val screen = Screen(
        id = "profile",
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(title = "Profile"),
                components = listOf(
                    ProfileRowComponent(
                        name = "Diego Ferreira",
                        email = "diego@example.com",
                        imageUrl = null
                    )
                )
            )
        )
    )

    DLearnTheme {
        ProfileScreenContent(
            uiState = ProfileUiState.Success(screen),
            onTabSelected = {},
            onItemClick = {},
            onClose = {},
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun ProfileScreenContentLoadingPreview() {
    DLearnTheme {
        ProfileScreenContent(
            uiState = ProfileUiState.Loading,
            onTabSelected = {},
            onItemClick = {},
            onClose = {},
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun ProfileScreenContentErrorPreview() {
    DLearnTheme {
        ProfileScreenContent(
            uiState = ProfileUiState.Error(RuntimeException("Failed to load profile")),
            onTabSelected = {},
            onItemClick = {},
            onClose = {},
            onRetry = {}
        )
    }
}
