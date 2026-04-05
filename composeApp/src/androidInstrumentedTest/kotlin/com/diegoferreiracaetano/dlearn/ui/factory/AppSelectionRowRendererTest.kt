package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSelectionRowRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppSelectionRowRenderer()

    @Test
    fun given_SelectionRowComponent_then_Render_should_displayTitleAndValue() {
        val title = "Language"
        val value = "English"
        val component = AppSelectionRowComponent(
            title = title,
            value = value,
            preferenceKey = "app_language",
            isSelected = true
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
        composeTestRule.onNodeWithText(value).assertExists()
    }
}
