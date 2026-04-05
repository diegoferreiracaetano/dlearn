package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMainContentComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class AppMainContentRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppMainContentRenderer()

    @Test
    fun given_AppMainContentComponent_then_Render_should_show_app_screen() {
        val component = AppMainContentComponent()

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(),
                    modifier = Modifier
                )
            }
        }

        // AppScreen should have a test tag or content that we can verify
        composeTestRule.onNodeWithTag("app_screen").assertExists()
    }
}
