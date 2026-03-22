package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialog
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppContent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    path: String,
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onDeeplink: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showClearCacheDialog by viewModel.showClearCacheDialog.collectAsStateWithLifecycle()

    LaunchedEffect(path) {
        viewModel.loadContent(path)
    }

    val actions = remember {
        ComponentActions(
            onBackClick = onBackClick,
            onRetry = viewModel::retry,
            onAction = { action ->
                when {
                    action.startsWith("http") || action.startsWith("https") -> onDeeplink(action)
                    action.startsWith("app://") -> onNavigate(action)
                    else -> viewModel.handleAction(action)
                }
            }
        )
    }

    AppContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier.fillMaxSize()
    )

    if (showClearCacheDialog) {
        AppDialog(
            onDismissRequest = { viewModel.dismissClearCacheDialog() },
            confirmButtonText = stringResource(Res.string.clear_cache_confirm),
            onConfirmClick = { viewModel.confirmClearCache() },
            dismissButtonText = stringResource(Res.string.clear_cache_cancel),
            onDismissClick = { viewModel.dismissClearCacheDialog() },
            title = stringResource(Res.string.clear_cache_dialog_title),
            description = stringResource(Res.string.clear_cache_dialog_description),
            // Usando um placeholder enquanto não temos o asset exato do mockup
            imageSource = AppImageSource.Resource(Res.drawable.apple) 
        )
    }
}
