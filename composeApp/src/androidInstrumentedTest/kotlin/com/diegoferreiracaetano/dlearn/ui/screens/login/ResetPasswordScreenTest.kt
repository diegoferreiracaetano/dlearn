package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class ResetPasswordScreenTest {

    @Test
    fun given_reset_password_screen_when_email_filled_and_button_clicked_should_onNextClick() = runComposeUiTest {
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

        onNodeWithTag(TestTags.Components.EMAIL_FIELD, useUnmergedTree = true).onChildAt(0).performTextInput(email)
        onNodeWithTag(TestTags.Components.NEXT_BUTTON).performClick()

        waitForIdle()

        verify { onNextClick() }
    }

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

        onNodeWithContentDescription("Voltar").performClick()

        waitForIdle()

        verify { onBackClick() }
    }
}
