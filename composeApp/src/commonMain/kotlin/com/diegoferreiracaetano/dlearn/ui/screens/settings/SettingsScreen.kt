package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialog
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.clear_cache_cancel
import dlearn.composeapp.generated.resources.clear_cache_confirm
import dlearn.composeapp.generated.resources.clear_cache_description
import dlearn.composeapp.generated.resources.clear_cache_title
import dlearn.composeapp.generated.resources.question
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    path: String,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(path) {
        if (path != NavigationRoutes.SETTINGS_CLEAR_CACHE) {
            viewModel.loadContent(path)
        }
    }


    val actions = remember {
        ComponentActions(
            onBackClick = onBackClick,
            onRetry = viewModel::retry,
            onSelectChanged = { key, value ->
                if(key != null && value != null)
                    viewModel.updatePreference(key, value)
            }
        )
    }

    SettingsScreenContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )

    if (path == NavigationRoutes.SETTINGS_CLEAR_CACHE) {
        SettingsCleanCache(
            onBackClick = onBackClick,
            onConfirmClick = {
                viewModel.confirmClearCache()
                onBackClick()
            }
        )
    }
}

@Composable
private fun SettingsCleanCache(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AppDialog(
        onDismissRequest = { onBackClick() },
        confirmButtonText = stringResource(Res.string.clear_cache_confirm),
        onConfirmClick = { onConfirmClick() },
        dismissButtonText = stringResource(Res.string.clear_cache_cancel),
        onDismissClick = { onBackClick() },
        title = stringResource(Res.string.clear_cache_title),
        description = stringResource(Res.string.clear_cache_description),
        imageSource = AppImageSource.Resource(Res.drawable.question)
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    uiState.Render(
        actions = actions,
        modifier = modifier
    )
}
