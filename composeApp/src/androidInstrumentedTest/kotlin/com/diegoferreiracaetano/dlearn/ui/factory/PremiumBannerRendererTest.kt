package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class PremiumBannerRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = PremiumBannerRenderer()

    @Test
    fun given_PremiumBannerComponent_then_Render_should_displayTitleAndDescription() {
        val title = "Go Premium"
        val description = "Unlock all features"
        val component = PremiumBannerComponent(title = title, description = description)

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
}
