package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
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
class SettingsViewModelTest {

    private val appRepository = mockk<AppRepository>()
    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val onConfigurationChanged = MutableSharedFlow<Unit>()
    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { preferencesRepository.onConfigurationChanged } returns onConfigurationChanged
        viewModel = SettingsViewModel(appRepository, preferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when loadContent is called should update state to Success`() = runTest {
        val path = "/settings"
        val screen = Screen(components = emptyList())
        coEvery { appRepository.execute(any()) } returns flowOf(screen)

        viewModel.loadContent(path)
        advanceUntilIdle()

        assertEquals(UIState.Success(screen), viewModel.uiState.value)
    }

    @Test
    fun `when loadContent fails should update state to Error`() = runTest {
        val path = "/settings"
        val exception = RuntimeException("error")
        coEvery { appRepository.execute(any()) } returns flow { throw exception }

        viewModel.loadContent(path)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UIState.Error)
        assertEquals(exception, state.throwable)
    }

    @Test
    fun `when retry is called should reload content`() = runTest {
        val path = "/settings"
        val screen = Screen(components = emptyList())
        coEvery { appRepository.execute(any()) } returns flowOf(screen)

        viewModel.loadContent(path)
        advanceUntilIdle()

        viewModel.retry()
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 2) { appRepository.execute(any()) }
    }

    @Test
    fun `when configuration changes should reload content`() = runTest {
        val path = "/settings"
        val screen = Screen(components = emptyList())
        coEvery { appRepository.execute(any()) } returns flowOf(screen)

        viewModel.loadContent(path)
        advanceUntilIdle()

        onConfigurationChanged.emit(Unit)
        advanceUntilIdle()

        @Suppress("IgnoredReturnValue")
        coVerify(exactly = 2) { appRepository.execute(any()) }
    }

    @Test
    fun `when updatePreference is called should delegate to repository`() {
        val key = "theme"
        val value = "dark"

        viewModel.updatePreference(key, value)

        coVerify(exactly = 1) { preferencesRepository.updatePreference(key, value) }
    }
}
