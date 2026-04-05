package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class MovieItemRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = MovieItemRenderer()

    @Test
    fun given_MovieItemComponent_then_Render_should_displayTitleAndHandleClick() {
        val title = "Movie Title"
        val movieId = "123"
        val onMovieClickMock = mockk<(String) -> Unit>(relaxed = true)
        val component = MovieItemComponent(id = movieId, title = title, imageUrl = "")

        composeTestRule.setContent {
            DLearnTheme {
                renderer.Render(
                    component = component,
                    actions = ComponentActions(onMovieClick = onMovieClickMock),
                    modifier = Modifier
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists().performClick()
        verify { onMovieClickMock(movieId) }
    }
}
