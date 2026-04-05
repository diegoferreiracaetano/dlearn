package com.diegoferreiracaetano.dlearn.ui.screens.auth.password

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class CreateNewPasswordScreenTest {

    private val viewModel: CreateNewPasswordViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<CreateNewPasswordUiState>(CreateNewPasswordUiState.Idle)

    @Test
    fun given_idle_state_when_password_filled_and_confirm_clicked_should_call_viewModel_changePassword() = runComposeUiTest {
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

        onNodeWithText("Senha").performTextInput(password)
        onNodeWithText("Confirmar nova senha").performTextInput(password)
        onNodeWithText("Alterar senha").performClick()

        verify { viewModel.changePassword(password) }
    }

    @Test
    fun given_loading_state_when_rendered_should_show_loading_indicator() = runComposeUiTest {
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

        onNodeWithTag("loading_indicator").assertExists()
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

        state.value = CreateNewPasswordUiState.Success("Senha alterada com sucesso")

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

        state.value = CreateNewPasswordUiState.Error(errorMessage)

        onNodeWithText(errorMessage).assertExists()
    }
}
