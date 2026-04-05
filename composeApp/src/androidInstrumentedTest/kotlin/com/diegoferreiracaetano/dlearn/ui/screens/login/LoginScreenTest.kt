package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class LoginScreenTest {

    private val viewModel: LoginViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)

    @Test
    fun given_idle_state_when_user_fills_credentials_and_clicks_login_should_call_viewModel_login() = runComposeUiTest {
        val email = "admin@dlearn.com"
        val password = "password123"
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                LoginScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    onForgotPasswordClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("E-mail").performTextInput(email)
        onNodeWithText("Senha").performTextInput(password)
        onNodeWithText("Entrar").performClick()

        verify { viewModel.login(email, password) }
    }

    @Test
    fun given_loading_state_when_rendered_should_disable_login_button() = runComposeUiTest {
        state.value = LoginUIState.Loading
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                LoginScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    onForgotPasswordClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Entrar").assertIsNotEnabled()
    }

    @Test
    fun given_success_state_when_rendered_should_trigger_onNavigateToHome() = runComposeUiTest {
        val onNavigateToHome: () -> Unit = mockk(relaxed = true)
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                LoginScreen(
                    onBackClick = {},
                    onNavigateToHome = onNavigateToHome,
                    onForgotPasswordClick = {},
                    viewModel = viewModel
                )
            }
        }

        state.value = LoginUIState.Success(true)

        verify { onNavigateToHome() }
    }

    @Test
    fun given_error_state_when_rendered_should_show_error_message() = runComposeUiTest {
        val errorMessage = "Invalid credentials"
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                LoginScreen(
                    onBackClick = {},
                    onNavigateToHome = {},
                    onForgotPasswordClick = {},
                    viewModel = viewModel
                )
            }
        }

        state.value = LoginUIState.Error(Exception(errorMessage))

        onNodeWithText(errorMessage).assertExists()
    }
}
