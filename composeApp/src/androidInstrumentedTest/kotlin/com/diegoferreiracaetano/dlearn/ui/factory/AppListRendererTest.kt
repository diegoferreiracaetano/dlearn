package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import org.junit.Rule
import org.junit.Test

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

        composeTestRule.onNodeWithTag(TestTags.Components.APP_LIST).assertExists()
        composeTestRule.onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }
}
