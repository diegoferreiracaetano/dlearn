package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class SignUpScreenTest {

    private val viewModel: SignUpViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<SignUpUIState>(SignUpUIState.Idle)

    @Test
    fun given_idle_state_when_register_clicked_should_call_viewModel_signUp() = runComposeUiTest {
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                SignUpScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Nome").performTextInput(name)
        onNodeWithText("E-mail").performTextInput(email)
        onNodeWithText("Senha").performTextInput(password)
        onNodeWithText("Criar conta").performClick()

        verify { viewModel.signUp(name, email, password) }
    }

    @Test
    fun given_loading_state_when_rendered_should_show_loading_indicator() = runComposeUiTest {
        state.value = SignUpUIState.Loading
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                SignUpScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun given_success_state_when_rendered_should_trigger_onNavigateToHome() = runComposeUiTest {
        val onNavigateToHome: () -> Unit = mockk(relaxed = true)
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                SignUpScreen(
                    onBackClick = {},
                    onNavigateToHome = onNavigateToHome,
                    viewModel = viewModel
                )
            }
        }

        state.value = SignUpUIState.Success(true)

        verify { onNavigateToHome() }
    }

    @Test
    fun given_error_state_when_rendered_should_show_error_message() = runComposeUiTest {
        val errorMessage = "Email already in use"
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                SignUpScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        state.value = SignUpUIState.Error(Exception(errorMessage))

        onNodeWithText(errorMessage).assertExists()
    }
}
