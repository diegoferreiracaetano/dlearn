package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.viewmodel.movie.MovieDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class MovieDetailScreenTest {

    private val viewModel: MovieDetailViewModel = mockk(relaxed = true)
    private val state = MutableStateFlow<UIState<Screen>>(UIState.Loading)

    @Test
    fun given_loading_state_when_rendered_should_show_loading_indicator() = runComposeUiTest {
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                MovieDetailScreen(
                    movieId = "1",
                    onBackClick = {},
                    onMovieClick = {},
                    onItemClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(TestTags.Components.LOADING_INDICATOR).assertExists()
    }

    @Test
    fun given_error_state_when_retry_clicked_should_call_viewModel_retry() = runComposeUiTest {
        state.value = UIState.Error(Exception("Error"))
        every { viewModel.uiState } returns state

        setContent {
            DLearnTheme {
                MovieDetailScreen(
                    movieId = "1",
                    onBackClick = {},
                    onMovieClick = {},
                    onItemClick = {},
                    viewModel = viewModel
                )
            }
        }

        onNodeWithTag(TestTags.Components.RETRY_BUTTON).performClick()

        verify { viewModel.retry() }
    }
}
