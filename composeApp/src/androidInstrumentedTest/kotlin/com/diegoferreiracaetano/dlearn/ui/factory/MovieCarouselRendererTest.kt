package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test

class MovieCarouselRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = MovieCarouselRenderer()

    @Test
    fun given_MovieCarouselComponent_then_Render_should_show_carousel_with_movies() {
        val component = MovieCarouselComponent(
            title = "Movie Carousel",
            items = listOf(
                MovieItemComponent(id = "1", title = "Movie 1", imageUrl = "url1")
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

        composeTestRule.onNodeWithText("Movie Carousel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()
    }
}
