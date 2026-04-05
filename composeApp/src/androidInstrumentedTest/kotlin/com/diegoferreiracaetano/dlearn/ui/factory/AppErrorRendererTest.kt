package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppErrorRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppErrorRenderer()

    @Test
    fun given_ErrorComponent_then_Render_should_ShowErrorMessageAndHandleRetryClick() {
        val onRetryMock = mockk<() -> Unit>(relaxed = true)
        val component = AppErrorComponent(throwable = Exception("Test Error"))

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onRetry = onRetryMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText("Tentar novamente").assertExists().performClick()
        verify { onRetryMock() }
    }
}
