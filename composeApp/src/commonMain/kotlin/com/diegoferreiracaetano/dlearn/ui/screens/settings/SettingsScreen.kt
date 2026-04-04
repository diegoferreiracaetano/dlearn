package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    path: String,
    onBackClick: () -> Unit = {},
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(path) {
        viewModel.loadContent(path)
    }

    val actions =
        remember {
            ComponentActions(
                onBackClick = onBackClick,
                onRetry = viewModel::retry,
                onClose = onClose,
                onSelectChanged = { key, value ->
                    if (key != null && value != null) {
                        viewModel.updatePreference(key, value)
                    }
                },
            )
        }

    SettingsScreenContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier,
    )
}

@Composable
fun SettingsScreenContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier,
) {
    uiState.Render(
        actions = actions,
        modifier = modifier,
    )
}

@Preview
@Composable
fun SettingsScreenSuccessPreview() {
    DLearnTheme {
        SettingsScreenContent(
            uiState = UIState.Success(Screen(components = listOf())),
            actions = ComponentActions(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun SettingsScreenLoadingPreview() {
    DLearnTheme {
        SettingsScreenContent(
            uiState = UIState.Loading,
            actions = ComponentActions(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun SettingsScreenErrorPreview() {
    DLearnTheme {
        SettingsScreenContent(
            uiState = UIState.Error(Exception("Error message")),
            actions = ComponentActions(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
