package com.diegoferreiracaetano.dlearn.profile

import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.ui.screens.profile.state.ProfileUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: FakeProfileRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeProfileRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProfile should update state to Success when repository returns data`() = runTest {
        val screen = Screen(id = "test", components = emptyList())
        repository.screenResult = screen
        
        viewModel = ProfileViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ProfileUiState.Success)
        assertEquals(screen, (viewModel.uiState.value as ProfileUiState.Success).screen)
    }

    @Test
    fun `fetchProfile should update state to Error when repository fails`() = runTest {
        repository.shouldFail = true
        
        viewModel = ProfileViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ProfileUiState.Error)
    }

    private class FakeProfileRepository : ProfileRepository {
        var screenResult: Screen? = null
        var shouldFail = false

        override fun getProfile() = flow {
            if (shouldFail) throw RuntimeException("Error")
            screenResult?.let { emit(it) }
        }
    }
}
