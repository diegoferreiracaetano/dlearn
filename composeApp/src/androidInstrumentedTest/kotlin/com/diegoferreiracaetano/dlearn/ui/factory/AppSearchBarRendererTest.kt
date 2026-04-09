package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class AppSearchBarRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppSearchBarRenderer()

    @Test
    fun given_AppSearchBarComponent_then_Render_should_show_search_bar_and_content() {
        val component = AppSearchBarComponent(
            placeholder = "Search Placeholder",
            actionUrl = "url",
            components = listOf(AppLoadingComponent)
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

        composeTestRule.onNodeWithText("Search Placeholder").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }
}
