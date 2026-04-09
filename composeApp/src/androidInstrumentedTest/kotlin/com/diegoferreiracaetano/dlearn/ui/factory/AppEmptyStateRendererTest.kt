package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class AppEmptyStateRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppEmptyStateRenderer()

    @Test
    fun given_EmptyStateComponent_then_Render_should_displayTitleAndDescription() {
        val title = "Empty Title"
        val description = "Empty Description"
        val component = AppEmptyStateComponent(title = title, description = description)

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
        composeTestRule.onNodeWithText(description).assertExists()
    }
}
