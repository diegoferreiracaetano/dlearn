package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class WelcomeScreenTest {

    private val viewModel: LoginViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)

    @Test
    fun given_idle_state_when_login_clicked_should_trigger_onLoginClick() = runComposeUiTest {
        val onLoginClick: () -> Unit = mockk(relaxed = true)
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                WelcomeScreen(
                    onSignUpClick = {},
                    onLoginClick = onLoginClick,
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Entrar").performClick()

        verify { onLoginClick() }
    }

    @Test
    fun given_idle_state_when_signup_clicked_should_trigger_onSignUpClick() = runComposeUiTest {
        val onSignUpClick: () -> Unit = mockk(relaxed = true)
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                WelcomeScreen(
                    onSignUpClick = onSignUpClick,
                    onLoginClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithText("Criar conta").performClick()

        verify { onSignUpClick() }
    }

    @Test
    fun given_idle_state_when_google_clicked_should_call_viewModel_signInWith() = runComposeUiTest {
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                WelcomeScreen(
                    onSignUpClick = {},
                    onLoginClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        // Assumindo que o botão Social tem uma testTag correspondente ao provider
        onNodeWithTag("google_button").performClick()

        verify { viewModel.signInWith(AccountProvider.GOOGLE) }
    }

    @Test
    fun given_loading_state_when_rendered_should_disable_buttons() = runComposeUiTest {
        state.value = LoginUIState.Loading
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                WelcomeScreen(
                    onSignUpClick = {},
                    onLoginClick = {},
                    onNavigateToHome = {},
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
                WelcomeScreen(
                    onSignUpClick = {},
                    onLoginClick = {},
                    onNavigateToHome = onNavigateToHome,
                    viewModel = viewModel
                )
            }
        }

        state.value = LoginUIState.Success(true)

        verify { onNavigateToHome() }
    }

    @Test
    fun given_error_state_when_rendered_should_show_error_message() = runComposeUiTest {
        val errorMessage = "Social login failed"
        every { viewModel.state } returns state

        setContent {
            DLearnTheme {
                WelcomeScreen(
                    onSignUpClick = {},
                    onLoginClick = {},
                    onNavigateToHome = {},
                    viewModel = viewModel
                )
            }
        }

        state.value = LoginUIState.Error(Exception(errorMessage))

        onNodeWithText(errorMessage).assertExists()
    }
}
