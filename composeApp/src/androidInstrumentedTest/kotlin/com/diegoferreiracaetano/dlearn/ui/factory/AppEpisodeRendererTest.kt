package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEpisodeComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class AppEpisodeRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppEpisodeRenderer()

    @Test
    fun given_AppEpisodeComponent_then_Render_should_displayTitleAndDescription() {
        val title = "Episode Title"
        val description = "Episode Description"
        val component = AppEpisodeComponent(
            id = "1",
            title = title,
            description = description,
            imageUrl = "",
            duration = "45m"
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

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(description).assertExists()
    }

    @Test
    fun given_AppEpisodeComponentWithAction_when_clicked_then_should_triggerAction() {
        val actionUrl = "dlearn://episode/1"
        val onActionMock = mockk<(String) -> Unit>(relaxed = true)
        val component = AppEpisodeComponent(
            id = "1",
            title = "Episode Title",
            description = "Description",
            imageUrl = "",
            duration = "45m",
            actionUrl = actionUrl
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

        composeTestRule.onNodeWithText("Episode Title").performClick()

        verify { onActionMock(actionUrl) }
    }
}
