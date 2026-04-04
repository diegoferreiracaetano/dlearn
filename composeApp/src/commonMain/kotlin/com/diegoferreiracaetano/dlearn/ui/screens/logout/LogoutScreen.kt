package com.diegoferreiracaetano.dlearn.ui.screens.logout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialog
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.logout.LogoutViewModel
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.cancel
import dlearn.composeapp.generated.resources.logout_confirm
import dlearn.composeapp.generated.resources.logout_description
import dlearn.composeapp.generated.resources.logout_title
import dlearn.composeapp.generated.resources.question
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LogoutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogoutViewModel = koinViewModel(),
) {
    LogoutContent(
        onBackClick = onBackClick,
        onConfirmClick = viewModel::logout,
        modifier = modifier,
    )
}

@Composable
fun LogoutContent(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppDialog(
        onDismissRequest = { onBackClick() },
        confirmButtonText = stringResource(Res.string.logout_confirm),
        onConfirmClick = { onConfirmClick() },
        dismissButtonText = stringResource(Res.string.cancel),
        onDismissClick = { onBackClick() },
        title = stringResource(Res.string.logout_title),
        description = stringResource(Res.string.logout_description),
        imageSource = AppImageSource.Resource(Res.drawable.question),
        modifier = modifier,
    )
}

@Preview
@Composable
fun LogoutScreenPreview() {
    DLearnTheme {
        LogoutContent(
            onBackClick = {},
            onConfirmClick = {},
        )
    }
}
