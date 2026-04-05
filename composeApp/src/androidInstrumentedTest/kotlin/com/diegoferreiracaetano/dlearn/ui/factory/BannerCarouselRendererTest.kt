package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class BannerCarouselRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = BannerCarouselRenderer()

    @Test
    fun given_BannerCarouselComponent_then_Render_should_show_carousel_with_banners() {
        val component = BannerCarouselComponent(
            title = "Banner Carousel",
            items = listOf(
                MovieItemComponent(id = "1", title = "Banner 1", imageUrl = "url1")
            )
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

        composeTestRule.onNodeWithText("Banner Carousel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Banner 1").assertIsDisplayed()
    }
}
