package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.SocialSignInUseCase
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.viewmodel.EffectChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val socialSignInUseCase: SocialSignInUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val state: StateFlow<LoginUIState> = _state.asStateFlow()

    private val effectChannel = EffectChannel<LoginEffect>()
    val effects = effectChannel.effects

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.Submit -> signIn { authRepository.login(action.email, action.password) }
            is LoginAction.SocialSignIn -> signIn { socialSignInUseCase(action.provider) }
            LoginAction.CheckSession -> checkSession()
        }
    }

    private fun signIn(source: () -> Flow<*>) {
        viewModelScope.launch {
            _state.value = LoginUIState.Loading

            source()
                .catch { effectChannel.send(LoginEffect.ShowError(it.toAppError())) }
                .collect { effectChannel.send(LoginEffect.NavigateToHome) }

            _state.value = LoginUIState.Idle
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            sessionManager.initialize()
            if (sessionManager.isLoggedIn.value) {
                effectChannel.send(LoginEffect.NavigateToHome)
            }
        }
    }
}
