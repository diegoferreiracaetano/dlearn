package com.diegoferreiracaetano.dlearn.ui.viewmodel.app

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import io.mockk.coEvery
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
class AppViewModelTest {

    private val repository = mockk<AppRepository>()
    private lateinit var viewModel: AppViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AppViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetch is called should update state to Success`() = runTest {
        val request = AppRequest(path = "/home")
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(request) } returns flowOf(screen)

        viewModel.fetch(request)
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when fetch with path is called should update state to Success`() = runTest {
        val path = "/home"
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(any()) } returns flowOf(screen)

        viewModel.fetch(path)
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when fetch fails should update state to Error`() = runTest {
        val request = AppRequest(path = "/home")
        val exception = RuntimeException("Network error")

        coEvery { repository.execute(request) } returns flow { throw exception }

        viewModel.fetch(request)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception, state.throwable)
    }

    @Test
    fun `when retry is called should execute last request again`() = runTest {
        val request = AppRequest(path = "/home")
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(request) } returns flowOf(screen)

        viewModel.fetch(request)
        advanceUntilIdle()

        viewModel.retry()
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when retry is called without last request should do nothing`() = runTest {
        viewModel.retry()
        assertEquals(UIState.Loading, viewModel.uiState.value)
    }
}
