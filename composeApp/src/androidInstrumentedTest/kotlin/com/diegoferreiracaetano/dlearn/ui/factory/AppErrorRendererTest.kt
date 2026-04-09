package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class AppErrorRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppErrorRenderer()

    @Test
    fun given_ErrorComponent_then_Render_should_ShowErrorMessageAndHandleRetryClick() {
        val onRetryMock = mockk<() -> Unit>(relaxed = true)
        val component = AppErrorComponent(error = AppError(AppErrorCode.NETWORK_ERROR))

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onRetry = onRetryMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithTag(TestTags.Components.RETRY_BUTTON).assertExists().performClick()
        verify { onRetryMock() }
    }
}
