package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FullScreenBannerRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = FullScreenBannerRenderer()

    @Test
    fun given_FullScreenBannerComponent_then_Render_should_displayTitleAndSubtitle() {
        val title = "Welcome to DLearn"
        val subtitle = "Learn anything, anywhere."
        val component = FullScreenBannerComponent(
            id = "banner_1",
            title = title,
            subtitle = subtitle,
            imageUrl = ""
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
        composeTestRule.onNodeWithText(subtitle).assertExists()
    }
}
