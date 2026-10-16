package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginAction
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginEffect
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class LoginScreenTest {

    private val viewModel: LoginViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)

    private val effectChannel = Channel<LoginEffect>(Channel.UNLIMITED)

    init {
        every { viewModel.effects } returns effectChannel.receiveAsFlow()
    }

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

        onNodeWithTag(
            TestTags.Components.EMAIL_FIELD,
            useUnmergedTree = true,
        ).onChildAt(0).performTextReplacement(email)
        onNodeWithTag(
            TestTags.Components.PASSWORD_FIELD,
            useUnmergedTree = true,
        ).onChildAt(0).performTextReplacement(password)
        onNodeWithTag(TestTags.Components.LOGIN_BUTTON).performClick()

        verify { viewModel.onAction(LoginAction.Submit(email, password)) }
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

        onNodeWithTag(TestTags.Components.LOGIN_BUTTON).assertIsNotEnabled()
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

        effectChannel.trySend(LoginEffect.NavigateToHome)

        waitForIdle()

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

        effectChannel.trySend(LoginEffect.ShowError(AppError(AppErrorCode.UNKNOWN_ERROR, errorMessage)))

        waitForIdle()

        onNodeWithTag(TestTags.Screens.LOGIN_SCREEN).assertExists()
    }
}
