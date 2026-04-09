package com.diegoferreiracaetano.dlearn.ui.screens.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.AppDialogContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImage
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
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

private val DialogImageSize = 120.dp
private val DialogSpacing = 16.dp
private val DialogButtonSpacing = 12.dp

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
    AppDialogContainer(
        onDismissRequest = { onBackClick() },
        modifier = modifier,
    ) {
        AppImage(
            source = AppImageSource.Resource(Res.drawable.question),
            modifier = Modifier.size(DialogImageSize),
            contentDescription = stringResource(Res.string.logout_title)
        )
        Spacer(modifier = Modifier.height(DialogSpacing))

        Text(
            text = stringResource(Res.string.logout_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(DialogSpacing))

        Text(
            text = stringResource(Res.string.logout_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(DialogSpacing))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DialogButtonSpacing)
        ) {
            AppButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel),
                onClick = { onBackClick() },
                type = ButtonType.SECONDARY,
                testTag = TestTags.Components.CANCEL_BUTTON
            )
            AppButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.logout_confirm),
                onClick = { onConfirmClick() },
                type = ButtonType.PRIMARY,
                testTag = TestTags.Components.CONFIRM_BUTTON
            )
        }
    }
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
