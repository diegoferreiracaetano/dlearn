package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _state = MutableStateFlow<SignUpUIState>(SignUpUIState.Idle)
    val state: StateFlow<SignUpUIState> = _state.asStateFlow()

    fun signUp(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            _state.value = SignUpUIState.Loading

            // Garante que qualquer sessão anterior (ex: admin) seja limpa antes do novo cadastro
            sessionManager.logout()

            authRepository
                .register(name, email, password)
                .catch { e ->
                    _state.value = SignUpUIState.Error(e)
                }.collect {
                    _state.value = SignUpUIState.Success(true)
                }
        }
    }
}
