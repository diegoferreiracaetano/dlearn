package com.diegoferreiracaetano.dlearn.ui.screens.auth.verify

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.VerifyAccountViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class VerifyAccountScreenTest {

    private val viewModel: VerifyAccountViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<VerifyAccountUiState>(VerifyAccountUiState.Idle)

    @Test
    fun given_idle_state_when_otp_filled_and_verify_clicked_should_call_viewModel_verifyOtp() = runComposeUiTest {
        val otp = "123456"
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                VerifyAccountScreen(
                    onBackClick = {},
                    onContinueClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(TestTags.Components.OTP_FIELD, useUnmergedTree = true).onChildAt(0).performTextInput(otp)
        onNodeWithTag(TestTags.Components.CONFIRM_BUTTON).performClick()

        verify { viewModel.verifyOtp(otp) }
    }

    @Test
    fun given_loading_state_when_rendered_should_disable_verify_button() = runComposeUiTest {
        state.value = VerifyAccountUiState.Loading
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                VerifyAccountScreen(
                    onBackClick = {},
                    onContinueClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(TestTags.Components.CONFIRM_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun given_success_state_when_rendered_should_trigger_onContinueClick() = runComposeUiTest {
        val onContinueClick: () -> Unit = mockk(relaxed = true)
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                VerifyAccountScreen(
                    onBackClick = {},
                    onContinueClick = onContinueClick,
                    viewModel = viewModel
                )
            }
        }

        state.value = VerifyAccountUiState.Success
        
        waitForIdle()

        verify { onContinueClick() }
    }

    @Test
    fun given_error_state_when_rendered_should_show_error_message() = runComposeUiTest {
        val errorMessage = "Invalid OTP"
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                VerifyAccountScreen(
                    onBackClick = {},
                    onContinueClick = {},
                    viewModel = viewModel
                )
            }
        }

        state.value = VerifyAccountUiState.Error(Exception(errorMessage))
        
        waitForIdle()

        onNodeWithText(errorMessage).assertExists()
    }
}
