package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.viewmodel.collectEffects
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
    fun `given valid data when submit should logout then register and navigate to home`() = runTest {
        val name = "Test"
        val email = "test@example.com"
        val password = "password"
        val effects = collectEffects(viewModel.effects)

        coEvery { authRepository.register(name, email, password) } returns flow {
            kotlinx.coroutines.delay(100)
            emit(AuthResponse(accessToken = "token"))
        }

        viewModel.onAction(SignUpAction.Submit(name, email, password))

        testDispatcher.scheduler.runCurrent()
        assertEquals(SignUpUIState.Loading, viewModel.state.value)

        advanceUntilIdle()

        coVerify(exactly = 1) { sessionManager.logout() }
        assertEquals(SignUpUIState.Idle, viewModel.state.value)
        assertEquals(listOf<SignUpEffect>(SignUpEffect.NavigateToHome), effects)
    }

    @Test
    fun `given error during register when submit should show the error and return to Idle`() = runTest {
        val exception = RuntimeException("Register failed")
        coEvery { authRepository.register(any(), any(), any()) } returns flow { throw exception }
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(SignUpAction.Submit("Test", "test@example.com", "password"))
        advanceUntilIdle()

        assertEquals(SignUpUIState.Idle, viewModel.state.value)
        assertEquals(listOf<SignUpEffect>(SignUpEffect.ShowError(exception.toAppError())), effects)
    }
}
