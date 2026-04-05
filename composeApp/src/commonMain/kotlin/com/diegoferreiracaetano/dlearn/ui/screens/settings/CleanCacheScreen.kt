package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialog
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.CleanCacheViewModel
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.clear_cache_cancel
import dlearn.composeapp.generated.resources.clear_cache_confirm
import dlearn.composeapp.generated.resources.clear_cache_description
import dlearn.composeapp.generated.resources.clear_cache_title
import dlearn.composeapp.generated.resources.question
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CleanCacheScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CleanCacheViewModel = koinViewModel(),
) {
    CleanCacheContent(
        onBackClick = onBackClick,
        onConfirmClick = viewModel::confirmClearCache,
        modifier = modifier,
    )
}

@Composable
fun CleanCacheContent(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppDialog(
        onDismissRequest = { onBackClick() },
        confirmButtonText = stringResource(Res.string.clear_cache_confirm),
        onConfirmClick = {
            onConfirmClick()
        },
        dismissButtonText = stringResource(Res.string.clear_cache_cancel),
        onDismissClick = { onBackClick() },
        title = stringResource(Res.string.clear_cache_title),
        description = stringResource(Res.string.clear_cache_description),
        imageSource = AppImageSource.Resource(Res.drawable.question),
        modifier = modifier,
    )
}
