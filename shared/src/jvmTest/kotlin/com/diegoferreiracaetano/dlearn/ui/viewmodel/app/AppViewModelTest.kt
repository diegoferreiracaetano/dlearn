package com.diegoferreiracaetano.dlearn.ui.viewmodel.app

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
        val request = AppPath.parse("/home")
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(request) } returns flowOf(screen)

        viewModel.onAction(AppAction.Load("/home"))
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when fetch with path is called should update state to Success`() = runTest {
        val path = "/home"
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(any()) } returns flowOf(screen)

        viewModel.onAction(AppAction.Load(path))
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when fetch fails should update state to Error`() = runTest {
        val request = AppPath.parse("/home")
        val exception = RuntimeException("Network error")

        coEvery { repository.execute(request) } returns flow { throw exception }

        viewModel.onAction(AppAction.Load("/home"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception.toAppError(), state.error)
    }

    @Test
    fun `when retry is called should execute last request again`() = runTest {
        val request = AppPath.parse("/home")
        val screen = Screen(components = emptyList())

        coEvery { repository.execute(request) } returns flowOf(screen)

        viewModel.onAction(AppAction.Load("/home"))
        advanceUntilIdle()

        viewModel.onAction(AppAction.Retry)
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when open is called should keep the current content while loading`() = runTest {
        val home = AppPath.parse("/home")
        val detail = AppPath.parse("/detail")
        val homeScreen = Screen(components = emptyList())
        val detailScreen = Screen(components = listOf(AppLoadingComponent))
        coEvery { repository.execute(home) } returns flowOf(homeScreen)
        coEvery { repository.execute(detail) } returns flow {
            delay(100)
            emit(detailScreen)
        }

        viewModel.onAction(AppAction.Load("/home"))
        advanceUntilIdle()

        viewModel.onAction(AppAction.Open("/detail"))
        testDispatcher.scheduler.runCurrent()
        assertEquals(UIState.Success(homeScreen), viewModel.uiState.value)

        advanceUntilIdle()
        assertEquals(UIState.Success(detailScreen), viewModel.uiState.value)
    }

    @Test
    fun `when retry follows an open should repeat the open request`() = runTest {
        val detail = AppPath.parse("/detail")
        val screen = Screen(components = listOf(AppLoadingComponent))
        coEvery { repository.execute(detail) } returns flowOf(screen)

        viewModel.onAction(AppAction.Open("/detail"))
        advanceUntilIdle()
        viewModel.onAction(AppAction.Retry)
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 2) { repository.execute(detail) }
    }

    @Test
    fun `when retry is called without last request should do nothing`() = runTest {
        viewModel.onAction(AppAction.Retry)
        assertEquals(UIState.Loading, viewModel.uiState.value)
    }
}
