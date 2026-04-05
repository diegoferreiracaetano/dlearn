package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ResetPasswordScreenTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_reset_password_screen_when_email_filled_and_button_clicked_should_trigger_onNextClick() = runComposeUiTest {
        val onNextClick: () -> Unit = mockk(relaxed = true)
        val email = "test@dlearn.com"

        setContent {
            DLearnTheme {
                ResetPasswordScreen(
                    onBackClick = {},
                    onNextClick = onNextClick
                )
            }
        }

        onNodeWithText("E-mail").performTextInput(email)
        onNodeWithText("Enviar código").performClick()

        verify { onNextClick() }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_reset_password_screen_when_back_clicked_should_trigger_onBackClick() = runComposeUiTest {
        val onBackClick: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                ResetPasswordScreen(
                    onBackClick = onBackClick,
                    onNextClick = {}
                )
            }
        }

        onNodeWithTag("back_button").performClick()

        verify { onBackClick() }
    }
}
