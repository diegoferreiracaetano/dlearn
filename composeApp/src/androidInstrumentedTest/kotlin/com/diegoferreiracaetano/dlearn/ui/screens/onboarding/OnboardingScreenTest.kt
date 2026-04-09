package com.diegoferreiracaetano.dlearn.ui.screens.onboarding

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.PAGE_CAROUSEL_NEXT_BUTTON_TAG
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OnboardingScreenTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun given_last_onboarding_page_when_finish_button_clicked_should_trigger_onFinish() = runComposeUiTest {
        val onFinish: () -> Unit = mockk(relaxed = true)

        setContent {
            DLearnTheme {
                OnboardingScreen(onFinish = onFinish)
            }
        }

        onNodeWithTag(PAGE_CAROUSEL_NEXT_BUTTON_TAG).performClick()
        waitForIdle()
        onNodeWithTag(PAGE_CAROUSEL_NEXT_BUTTON_TAG).performClick()
        waitForIdle()

        onNodeWithTag(PAGE_CAROUSEL_NEXT_BUTTON_TAG).performClick()

        verify { onFinish() }
    }
}
