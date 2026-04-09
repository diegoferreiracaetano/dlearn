package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class FooterRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = FooterRenderer()

    @Test
    fun given_FooterComponent_when_Rendered_then_should_displayLabelAndHandleClick() {
        val label = "Click Me"
        val actionUrl = "action/url"
        val onItemClickMock = mockk<(String) -> Unit>(relaxed = true)
        val component = FooterComponent(label = label, actionUrl = actionUrl)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onItemClick = onItemClickMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(label).assertExists().performClick()
        verify { onItemClickMock(actionUrl) }
    }
}
