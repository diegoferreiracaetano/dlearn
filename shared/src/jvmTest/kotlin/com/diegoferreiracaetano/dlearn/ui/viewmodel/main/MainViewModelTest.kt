package com.diegoferreiracaetano.dlearn.ui.viewmodel.main

import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
class MainViewModelTest {

    private val mainRepository = mockk<MainRepository>()
    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val onConfigurationChanged = MutableSharedFlow<Unit>()
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { preferencesRepository.onConfigurationChanged } returns onConfigurationChanged
        // MainViewModel calls loadMain in init
        coEvery { mainRepository.getMain() } returns flowOf(Screen(components = emptyList()))
        viewModel = MainViewModel(mainRepository, preferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized should load main content`() = runTest {
        advanceUntilIdle()
        coVerify(exactly = 1) { mainRepository.getMain() }
        assertTrue(viewModel.uiState.value is UIState.Success)
    }

    @Test
    fun `when loadMain fails should update state to Error`() = runTest {
        val exception = RuntimeException("Main error")
        coEvery { mainRepository.getMain() } returns flow { throw exception }

        viewModel.loadMain()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception, state.throwable)
    }

    @Test
    fun `when configuration changes should reload main content`() = runTest {
        advanceUntilIdle()
        
        onConfigurationChanged.emit(Unit)
        advanceUntilIdle()

        coVerify(exactly = 2) { mainRepository.getMain() }
    }

    @Test
    fun `when retry is called should reload main content`() = runTest {
        advanceUntilIdle()
        
        viewModel.retry()
        advanceUntilIdle()

        coVerify(exactly = 2) { mainRepository.getMain() }
    }
}
