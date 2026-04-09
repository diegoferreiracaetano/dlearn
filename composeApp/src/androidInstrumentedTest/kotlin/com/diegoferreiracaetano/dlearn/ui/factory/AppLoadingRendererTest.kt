package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import org.junit.Rule
import org.junit.Test

class AppLoadingRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppLoadingRenderer()

    @Test
    fun given_LoadingComponent_then_Render_should_showLoadingIndicator() {
        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = AppLoadingComponent,
                    actions = ComponentActions(),
                    modifier = androidx.compose.ui.Modifier
                )
            }
        }

        composeTestRule.onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }
}
