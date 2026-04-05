package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    private val viewModel: SettingsViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)

    @Test
    fun given_loading_state_when_rendered_should_show_loading_indicator() = runComposeUiTest {
        every { viewModel.uiState } returns uiState

        setContent {
            DLearnTheme {
                SettingsScreen(
                    path = "settings",
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun given_error_state_when_retry_clicked_should_call_viewModel_retry() = runComposeUiTest {
        uiState.value = UIState.Error(Exception("Error"))
        every { viewModel.uiState } returns uiState

        setContent {
            DLearnTheme {
                SettingsScreen(
                    path = "settings",
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Tentar novamente").performClick()

        verify { viewModel.retry() }
    }
}
