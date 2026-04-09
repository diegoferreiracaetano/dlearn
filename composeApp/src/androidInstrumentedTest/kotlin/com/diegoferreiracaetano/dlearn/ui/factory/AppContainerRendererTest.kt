package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import org.junit.Rule
import org.junit.Test

class AppContainerRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppContainerRenderer()

    @Test
    fun given_AppContainerComponent_then_Render_should_show_content() {
        val component = AppContainerComponent(
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

        composeTestRule.onNodeWithTag(TestTags.Components.APP_CONTAINER).assertExists()
        composeTestRule.onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }
}
