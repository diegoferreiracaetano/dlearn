package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.auth.SocialSignInUseCase
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.ui.viewmodel.collectEffects
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
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
    fun `given valid credentials when submit should go through Loading then navigate to home`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val effects = collectEffects(viewModel.effects)

        // fluxo com atraso para conseguir observar o Loading
        coEvery { authRepository.login(email, password) } returns flow {
            kotlinx.coroutines.delay(100)
            emit(AuthResponse(accessToken = "token"))
        }

        viewModel.onAction(LoginAction.Submit(email, password))

        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginUIState.Loading, viewModel.state.value)

        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertEquals(listOf<LoginEffect>(LoginEffect.NavigateToHome), effects)
    }

    @Test
    fun `given invalid credentials when submit should show the error and return to Idle`() = runTest {
        val exception = RuntimeException("Invalid credentials")
        coEvery { authRepository.login("test@example.com", "wrong") } returns flow { throw exception }
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.Submit("test@example.com", "wrong"))
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertEquals(listOf<LoginEffect>(LoginEffect.ShowError(exception.toAppError())), effects)
    }

    @Test
    fun `given a provider when social sign in succeeds should navigate to home`() = runTest {
        val provider = AccountProvider.GOOGLE
        coEvery { socialSignInUseCase(provider) } returns flow { emit(Unit) }
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.SocialSignIn(provider))
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertEquals(listOf<LoginEffect>(LoginEffect.NavigateToHome), effects)
    }

    @Test
    fun `given a provider when social sign in fails should show the error`() = runTest {
        val provider = AccountProvider.GOOGLE
        val exception = RuntimeException("Error")
        coEvery { socialSignInUseCase(provider) } returns flow { throw exception }
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.SocialSignIn(provider))
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertEquals(listOf<LoginEffect>(LoginEffect.ShowError(exception.toAppError())), effects)
    }

    @Test
    fun `given a provider when social sign in is cancelled should return to Idle without effects`() = runTest {
        val provider = AccountProvider.GOOGLE
        coEvery { socialSignInUseCase(provider) } returns emptyFlow()
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.SocialSignIn(provider))
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `given session is logged in when checking session should navigate to home`() = runTest {
        coEvery { sessionManager.initialize() } returns Unit
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(true)
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.CheckSession)
        advanceUntilIdle()

        assertEquals(listOf<LoginEffect>(LoginEffect.NavigateToHome), effects)
    }

    @Test
    fun `given session is not logged in when checking session should not emit effects`() = runTest {
        coEvery { sessionManager.initialize() } returns Unit
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(false)
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(LoginAction.CheckSession)
        advanceUntilIdle()

        assertEquals(LoginUIState.Idle, viewModel.state.value)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `given effect emitted before the screen collects when collecting later should still receive it`() = runTest {
        coEvery { sessionManager.initialize() } returns Unit
        coEvery { sessionManager.isLoggedIn } returns MutableStateFlow(true)

        viewModel.onAction(LoginAction.CheckSession)
        advanceUntilIdle()

        assertEquals(LoginEffect.NavigateToHome, viewModel.effects.first())
    }
}
