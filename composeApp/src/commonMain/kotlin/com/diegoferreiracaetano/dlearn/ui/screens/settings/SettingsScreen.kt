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
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppContent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    path: String,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(path) {
        viewModel.loadContent(path)
    }

    val actions = remember {
        ComponentActions(
            onBackClick = onBackClick,
            onRetry = viewModel::retry,
            onSelectChanged = { key, value ->
                if(key != null && value != null)
                    viewModel.updatePreference(key, value)
            },
            onAction = viewModel::handleAction
        )
    }

    SettingsScreenContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )

//    if (showClearCacheDialog) {
//        AppDialog(
//            onDismissRequest = { viewModel.dismissClearCacheDialog() },
//            confirmButtonText = stringResource(Res.string.clear_cache_confirm),
//            onConfirmClick = { viewModel.confirmClearCache() },
//            dismissButtonText = stringResource(Res.string.clear_cache_cancel),
//            onDismissClick = { viewModel.dismissClearCacheDialog() },
//            title = stringResource(Res.string.clear_cache_dialog_title),
//            description = stringResource(Res.string.clear_cache_dialog_description),
//            // Usando um placeholder enquanto não temos o asset exato do mockup
//            imageSource = AppImageSource.Resource(Res.drawable.apple)
//        )
//    }
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

@Preview
@Composable
fun SettingsNotificationsPreview() {
    val mockScreen = Screen(
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(
                    title = "Notificações",
                ),
                components = listOf(
                    AppSwitchRowComponent(
                        title = "Notificações Push",
                        subtitle = "Receba avisos sobre novos conteúdos",
                        icon = AppIconType.NOTIFICATIONS,
                        preferenceKey = "pref_notifications",
                        isChecked = true
                    ),
                    AppSwitchRowComponent(
                        title = "Ofertas e Promoções",
                        subtitle = "Fique por dentro de descontos exclusivos",
                        icon = AppIconType.WORKSPACE_PREMIUM,
                        preferenceKey = "pref_offers",
                        isChecked = false
                    )
                )
            )
        )
    )

    DLearnTheme {
        SettingsScreenContent(
            uiState = UIState.Success(mockScreen),
            actions = ComponentActions(onAction = {}),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun SettingsLanguagePreview() {
    val mockScreen = Screen(
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(
                    title = "Idioma",
                ),
                components = listOf(
                    AppSelectionRowComponent(
                        title = "Português (Brasil)",
                        preferenceKey = "pref_language",
                        value = "pt-BR",
                        isSelected = true
                    ),
                    AppSelectionRowComponent(
                        title = "English (US)",
                        preferenceKey = "pref_language",
                        value = "en-US",
                        isSelected = false
                    ),
                    AppSelectionRowComponent(
                        title = "Español",
                        preferenceKey = "pref_language",
                        value = "es-ES",
                        isSelected = false
                    )
                )
            )
        )
    )

    DLearnTheme {
        SettingsScreenContent(
            uiState = UIState.Success(mockScreen),
            actions = ComponentActions(onAction = {}),
            modifier = Modifier.fillMaxSize()
        )
    }
}
