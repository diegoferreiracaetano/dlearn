package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appRepository: AppRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val state: StateFlow<LoginUIState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUIState.Loading
            
            val params = mapOf(
                "email" to email,
                "password" to password
            )

            appRepository.execute("/auth/login", params)
                .catch { e -> 
                    _state.value = LoginUIState.Error(e) 
                }
                .collect {
                    // O AppRepositoryRemote já persiste o token no SessionManager 
                    // ao interceptar a resposta do login.
                    _state.value = LoginUIState.Success(sessionManager.isLoggedIn.value)
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
