package com.diegoferreiracaetano.dlearn.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(onItemClick, onTabSelected, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }

    ProfileContent(uiState, actions, modifier)
}

@Composable
fun ProfileContent(
    uiState: UIState,
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
fun ProfileContentPreview() {
    val screen = Screen(
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
        ProfileContent(
            uiState = UIState.Success(screen),
            actions = ComponentActions(),
            modifier = Modifier
        )
    }
}

