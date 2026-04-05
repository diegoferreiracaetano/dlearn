package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppListRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppListRenderer()

    @Test
    fun given_AppListComponent_with_children_then_Render_should_show_list_and_children() {
        val component = AppListComponent(
            components = listOf(AppLoadingComponent)
        )

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = androidx.compose.ui.Modifier
                )
            }
        }

        // AppList (from Design System) should have a test tag. 
        // Based on AppListRenderer, it uses AppList.
        composeTestRule.onNodeWithTag("app_list").assertExists()
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }
}
