package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class AppTopBarRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppTopBarRenderer()

    @Test
    fun given_TopBarComponent_then_Render_should_displayTitle() {
        val title = "Page Title"
        val component = AppTopBarComponent(title = title)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
    }
}
