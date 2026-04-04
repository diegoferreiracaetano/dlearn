package com.diegoferreiracaetano.dlearn.ui.viewmodel.logout

import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    private val sessionManager: SessionManager = mockk()
    private lateinit var viewModel: LogoutViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LogoutViewModel(sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `logout should call sessionManager logout`() = runTest {
        coEvery { sessionManager.logout() } returns Unit

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { sessionManager.logout() }
    }
}
