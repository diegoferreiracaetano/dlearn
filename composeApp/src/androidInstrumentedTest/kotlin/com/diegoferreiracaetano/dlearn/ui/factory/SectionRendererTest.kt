package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionItem
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class SectionRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = SectionRenderer()

    @Test
    fun given_SectionComponent_then_Render_should_show_title_and_items() {
        val component = SectionComponent(
            title = "Section Title",
            items = listOf(
                SectionItem(id = "1", label = "Label 1", value = "Value 1"),
                SectionItem(id = "2", label = "Label 2", value = "Value 2")
            )
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText("Section Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Label 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Value 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Label 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Value 2").assertIsDisplayed()
    }
}
