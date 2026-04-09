package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class ChipGroupRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = ChipGroupRenderer()

    @Test
    fun given_ChipGroupComponent_then_Render_should_show_chips() {
        val component = ChipGroupComponent(
            items = listOf(
                ChipItem(id = "1", label = "Chip 1", actionUrl = "url1"),
                ChipItem(id = "2", label = "Chip 2", actionUrl = "url2")
            ),
            cleanUrl = "clean"
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

        composeTestRule.onNodeWithText("Chip 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chip 2").assertIsDisplayed()
    }
}
