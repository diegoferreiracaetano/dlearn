package com.diegoferreiracaetano.dlearn.ui.screens.logout

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.logout.LogoutViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class LogoutScreenTest {

    private val viewModel: LogoutViewModel = mockk(relaxed = true)

    @Test
    fun given_logout_screen_when_confirm_clicked_should_call_viewModel_logout() = runComposeUiTest {
        setContent {
            DLearnTheme {
                LogoutScreen(
                    onBackClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Sim, sair").performClick()

        verify { viewModel.logout() }
    }

    @Test
    fun given_logout_screen_when_cancel_clicked_should_trigger_onBackClick() = runComposeUiTest {
        val onBackClick: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                LogoutScreen(
                    onBackClick = onBackClick,
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Cancelar").performClick()

        verify { onBackClick() }
    }
}
