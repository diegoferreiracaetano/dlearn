package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpAction
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpEffect
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class SignUpScreenTest {

    private val viewModel: SignUpViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<SignUpUIState>(SignUpUIState.Idle)

    private val effectChannel = Channel<SignUpEffect>(Channel.UNLIMITED)

    init {
        every { viewModel.effects } returns effectChannel.receiveAsFlow()
    }

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

        onNodeWithTag(TestTags.Components.NAME_FIELD, useUnmergedTree = true).onChildAt(0).performTextInput(name)
        onNodeWithTag(TestTags.Components.EMAIL_FIELD, useUnmergedTree = true).onChildAt(0).performTextInput(email)
        onNodeWithTag(
            TestTags.Components.PASSWORD_FIELD,
            useUnmergedTree = true,
        ).onChildAt(0).performTextInput(password)
        onNodeWithTag(TestTags.Components.SIGN_UP_BUTTON).performClick()

        verify { viewModel.onAction(SignUpAction.Submit(name, email, password)) }
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

        effectChannel.trySend(SignUpEffect.NavigateToHome)

        waitForIdle()

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

        effectChannel.trySend(SignUpEffect.ShowError(AppError(AppErrorCode.UNKNOWN_ERROR, errorMessage)))

        waitForIdle()

        onNodeWithTag(TestTags.Screens.SIGN_UP_SCREEN).assertExists()
    }
}
