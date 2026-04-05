package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppFeedbackComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppFeedbackRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppFeedbackRenderer()

    @Test
    fun given_FeedbackComponent_then_Render_should_displayContentAndHandleRetry() {
        val title = "Feedback Title"
        val description = "Feedback Description"
        val primaryText = "Retry Action"
        val onRetryMock = mockk<() -> Unit>(relaxed = true)
        val component = AppFeedbackComponent(
            title = title,
            description = description,
            primaryText = primaryText
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onRetry = onRetryMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(description).assertExists()
        composeTestRule.onNodeWithText(primaryText).performClick()
        verify { onRetryMock() }
    }
}
