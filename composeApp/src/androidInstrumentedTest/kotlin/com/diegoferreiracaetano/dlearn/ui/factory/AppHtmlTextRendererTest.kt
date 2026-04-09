package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppHtmlTextComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class AppHtmlTextRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppHtmlTextRenderer()

    @Test
    fun given_HtmlTextComponent_then_Render_should_displayHtmlContent() {
        val htmlContent = "Hello World"
        val component = AppHtmlTextComponent(html = htmlContent)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(htmlContent).assertExists()
    }
}
