package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class SignUpViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private lateinit var viewModel: SignUpViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignUpViewModel(authRepository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid data when signUp is called should logout then register and update state to Success`() = runTest {
        val name = "Test"
        val email = "test@example.com"
        val password = "password"
        val response = AuthResponse(accessToken = "token")

        coEvery { authRepository.register(name, email, password) } returns flow {
            kotlinx.coroutines.delay(100)
            emit(response)
        }

        viewModel.signUp(name, email, password)

        testDispatcher.scheduler.runCurrent()
        assertEquals(SignUpUIState.Loading, viewModel.state.value)

        advanceUntilIdle()

        coVerify(exactly = 1) { sessionManager.logout() }
        assertEquals(SignUpUIState.Success(true), viewModel.state.value)
    }

    @Test
    fun `given error during register when signUp is called should update state to Error`() = runTest {
        val exception = RuntimeException("Register failed")
        coEvery { authRepository.register(any(), any(), any()) } returns flow { throw exception }

        viewModel.signUp("Test", "test@example.com", "password")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is SignUpUIState.Error)
        assertEquals(exception, state.error)
    }

    @Test
    fun `SignUpUIState components test`() {
        assertEquals(SignUpUIState.Idle, SignUpUIState.Idle)
        assertEquals(SignUpUIState.Loading, SignUpUIState.Loading)
        val success = SignUpUIState.Success(true)
        assertTrue(success.isLoggedIn)
    }
}
