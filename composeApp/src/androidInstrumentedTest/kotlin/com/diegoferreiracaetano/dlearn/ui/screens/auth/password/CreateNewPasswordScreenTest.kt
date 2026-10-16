package com.diegoferreiracaetano.dlearn.ui.screens.auth.password

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.ui.util.TestTags.Components.CONFIRM_PASSWORD_FIELD
import com.diegoferreiracaetano.dlearn.ui.util.TestTags.Components.FINISH_BUTTON
import com.diegoferreiracaetano.dlearn.ui.util.TestTags.Components.PASSWORD_FIELD
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordAction
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordEffect
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class CreateNewPasswordScreenTest {

    private val viewModel: CreateNewPasswordViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<CreateNewPasswordUiState>(CreateNewPasswordUiState.Idle)

    private val effectChannel = Channel<CreateNewPasswordEffect>(Channel.UNLIMITED)

    init {
        every { viewModel.effects } returns effectChannel.receiveAsFlow()
    }

    @Test
    fun given_idle_state_when_password_filled_and_confirm_clicked_should_call_changePassword() = runComposeUiTest {
        val password = "newPassword123"
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                CreateNewPasswordScreen(
                    onBackClick = {},
                    onSuccess = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(PASSWORD_FIELD, useUnmergedTree = true)
            .onChildAt(0)
            .performTextInput(password)
        onNodeWithTag(CONFIRM_PASSWORD_FIELD, useUnmergedTree = true)
            .onChildAt(0)
            .performTextInput(password)

        onNodeWithTag(FINISH_BUTTON).performClick()

        verify { viewModel.onAction(CreateNewPasswordAction.ChangePassword(password)) }
    }

    @Test
    fun given_loading_state_when_rendered_should_disable_finish_button() = runComposeUiTest {
        state.value = CreateNewPasswordUiState.Loading
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                CreateNewPasswordScreen(
                    onBackClick = {},
                    onSuccess = {},
                    viewModel = viewModel
                )
            }
        }

        waitForIdle()

        onNodeWithTag(FINISH_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun given_success_state_when_rendered_should_trigger_onSuccess() = runComposeUiTest {
        val onSuccess: () -> Unit = mockk(relaxed = true)
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                CreateNewPasswordScreen(
                    onBackClick = {},
                    onSuccess = onSuccess,
                    viewModel = viewModel
                )
            }
        }

        effectChannel.trySend(CreateNewPasswordEffect.Changed("Senha alterada com sucesso"))

        waitForIdle()

        verify { onSuccess() }
    }

    @Test
    fun given_error_state_when_rendered_should_show_error_message() = runComposeUiTest {
        val errorMessage = "Passwords do not match"
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                CreateNewPasswordScreen(
                    onBackClick = {},
                    onSuccess = {},
                    viewModel = viewModel
                )
            }
        }

        effectChannel.trySend(
            CreateNewPasswordEffect.ShowError(AppError(AppErrorCode.UNKNOWN_ERROR, errorMessage)),
        )

        waitForIdle()

        onNodeWithText(errorMessage).assertExists()
    }
}
