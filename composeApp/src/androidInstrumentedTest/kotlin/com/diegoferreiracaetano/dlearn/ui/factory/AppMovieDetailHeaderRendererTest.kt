package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import org.junit.Test


@ExperimentalTestApi
class AppMovieDetailHeaderRendererTest {

    private val renderer = AppMovieDetailHeaderRenderer()

    @Test
    fun given_MovieDetailHeaderComponent_then_Render_should_displayTitleAndYear() = runComposeUiTest {
        val title = "Inception"
        val year = "2010"
        val duration = "2h 28m"
        val genre = "Action"

        val component = AppMovieDetailHeaderComponent(
            title = title,
            imageUrl = "",
            year = year,
            duration = duration,
            genre = genre
        )

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalSnackbarHostState provides snackbarHostState,
            ) {
                DLearnTheme {
                    renderer.Render(
                        component = component,
                        actions = ComponentActions(),
                        modifier = Modifier
                    )
                }
            }
        }

        onNodeWithText(year).assertExists()
        onNodeWithText(duration).assertExists()
        onNodeWithText(genre).assertExists()
    }
}
