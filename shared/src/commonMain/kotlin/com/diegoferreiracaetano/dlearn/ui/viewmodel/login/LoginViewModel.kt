package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.SocialSignInUseCase
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val socialSignInUseCase: SocialSignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val state: StateFlow<LoginUIState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUIState.Loading

            authRepository.login(email, password)
                .catch { e ->
                    _state.value = LoginUIState.Error(e) 
                }
                .collect {
                    _state.value = LoginUIState.Success(sessionManager.isLoggedIn.value)
                }
        }
    }

    fun signInWith(provider: AccountProvider) {
        viewModelScope.launch {
            _state.value = LoginUIState.Loading
            
            socialSignInUseCase(provider)
                .catch { e ->
                    _state.value = LoginUIState.Error(e)
                }
                .collect {
                    _state.value = LoginUIState.Success(sessionManager.isLoggedIn.value)
                }
            
            if (_state.value is LoginUIState.Loading) {
                _state.value = LoginUIState.Idle
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            sessionManager.initialize()
            if (sessionManager.isLoggedIn.value) {
                _state.value = LoginUIState.Success(true)
            }
        }
    }
}
