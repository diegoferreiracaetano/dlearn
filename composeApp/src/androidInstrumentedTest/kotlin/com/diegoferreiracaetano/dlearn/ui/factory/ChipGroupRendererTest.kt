package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
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

    @Test
    fun given_ChipGroupComponentWithDropDown_when_optionSelected_then_should_triggerAction() {
        val actionUrl = "dlearn://season/1"
        val onActionMock = mockk<(String) -> Unit>(relaxed = true)
        val component = ChipGroupComponent(
            items = listOf(
                ChipItem(
                    id = "1",
                    label = "Seasons",
                    actionUrl = "",
                    hasDropDown = true,
                    options = listOf(
                        ChipItem(id = "s1", label = "Season 1", actionUrl = actionUrl)
                    )
                )
            ),
            cleanUrl = "clean"
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onAction = onActionMock),
                    modifier = Modifier
                )
            }
        }

        // Open DropDown
        composeTestRule.onNodeWithText("Seasons").performClick()

        // Select Option
        composeTestRule.onNodeWithText("Season 1").performClick()

        verify { onActionMock(actionUrl) }
    }
}
