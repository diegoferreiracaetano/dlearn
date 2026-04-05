package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppExpandableSectionComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppExpandableSectionRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppExpandableSectionRenderer()

    @Test
    fun given_ExpandableSectionComponent_then_Render_should_displayTitleAndText() {
        val title = "More Information"
        val text = "This is a detailed description that can be expanded."
        val component = AppExpandableSectionComponent(title = title, text = text)

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists().performClick()
        composeTestRule.onNodeWithText(text).assertExists()
    }
}
