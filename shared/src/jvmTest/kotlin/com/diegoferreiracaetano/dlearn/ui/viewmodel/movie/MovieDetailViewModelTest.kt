package com.diegoferreiracaetano.dlearn.ui.viewmodel.movie

import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private val repository = mockk<MovieDetailRepository>()
    private val movieId = "123"
    private lateinit var viewModel: MovieDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.execute(any()) } returns flowOf(Screen(components = emptyList()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized should fetch movie details`() = runTest {
        viewModel = MovieDetailViewModel(movieId, repository)
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 1) { repository.execute(any()) }
        assertTrue(viewModel.uiState.value is UIState.Success)
    }

    @Test
    fun `when fetch fails should update state to Error`() = runTest {
        val exception = RuntimeException("Movie error")
        coEvery { repository.execute(any()) } returns flow { throw exception }

        viewModel = MovieDetailViewModel(movieId, repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception.toAppError(), state.error)
    }

    @Test
    fun `when retry is called should fetch details again`() = runTest {
        viewModel = MovieDetailViewModel(movieId, repository)
        advanceUntilIdle()

        viewModel.onAction(MovieDetailAction.Retry)
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 2) { repository.execute(any()) }
    }

    @Test
    fun `when execute is called with full url should fetch new content`() = runTest {
        viewModel = MovieDetailViewModel(movieId, repository)
        advanceUntilIdle()

        val fullUrl = "/movies/456"
        viewModel.onAction(MovieDetailAction.Open(fullUrl))
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 2) { repository.execute(any()) }
    }

    @Test
    fun `when execute with full url fails should update state to Error`() = runTest {
        viewModel = MovieDetailViewModel(movieId, repository)
        advanceUntilIdle()

        val exception = RuntimeException("fail")
        coEvery { repository.execute(any()) } returns flow { throw exception }

        viewModel.onAction(MovieDetailAction.Open("/fail"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception.toAppError(), state.error)
    }
}
