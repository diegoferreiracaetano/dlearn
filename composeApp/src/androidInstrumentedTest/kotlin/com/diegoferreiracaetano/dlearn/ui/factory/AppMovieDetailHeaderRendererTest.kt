package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class AppMovieDetailHeaderRendererTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val renderer = AppMovieDetailHeaderRenderer()

    @Test
    fun given_MovieDetailHeaderComponent_then_Render_should_displayTitleAndYear() {
        val title = "Inception"
        val year = "2010"
        val component = AppMovieDetailHeaderComponent(
            title = title,
            imageUrl = "",
            year = year,
            duration = "2h 28m",
            genre = "Action"
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
        composeTestRule.onNodeWithText(year).assertExists()
    }
}
