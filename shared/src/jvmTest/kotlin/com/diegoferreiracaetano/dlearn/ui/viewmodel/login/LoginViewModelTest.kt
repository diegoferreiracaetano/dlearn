package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.auth.SocialSignInUseCase
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class LoginViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private val socialSignInUseCase = mockk<SocialSignInUseCase>()
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(authRepository, sessionManager, socialSignInUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid credentials when login is called should update state to Success`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val response = AuthResponse(accessToken = "token")
        
        // Use a delayed flow to capture the Loading state
        coEvery { authRepository.login(email, password) } returns flow {
            kotlinx.coroutines.delay(100)
            emit(response)
        }
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(true)

        viewModel.login(email, password)
        
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginUIState.Loading, viewModel.state.value)
        
        advanceUntilIdle()
        
        assertEquals(LoginUIState.Success(true), viewModel.state.value)
    }

    @Test
    fun `given invalid credentials when login is called should update state to Error`() = runTest {
        val email = "test@example.com"
        val password = "wrong"
        val exception = RuntimeException("Invalid credentials")
        coEvery { authRepository.login(email, password) } returns flow { throw exception }

        viewModel.login(email, password)
        advanceUntilIdle()
        
        val state = viewModel.state.value
        assertTrue(state is LoginUIState.Error)
        assertEquals(exception, (state as LoginUIState.Error).throwable)
    }

    @Test
    fun `given a provider when signInWith is called should update state to Success`() = runTest {
        val provider = AccountProvider.GOOGLE
        coEvery { socialSignInUseCase(provider) } returns flow {
            emit(Unit)
        }
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(true)

        viewModel.signInWith(provider)
        advanceUntilIdle()
        
        assertEquals(LoginUIState.Success(true), viewModel.state.value)
    }

    @Test
    fun `given session is logged in when checkSession is called should update state to Success`() = runTest {
        coEvery { sessionManager.initialize() } returns Unit
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(true)

        viewModel.checkSession()
        advanceUntilIdle()

        assertEquals(LoginUIState.Success(true), viewModel.state.value)
    }

    @Test
    fun `given session is not logged in when checkSession is called should remain in Idle`() = runTest {
        coEvery { sessionManager.initialize() } returns Unit
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(false)

        viewModel.checkSession()
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
    }
}
