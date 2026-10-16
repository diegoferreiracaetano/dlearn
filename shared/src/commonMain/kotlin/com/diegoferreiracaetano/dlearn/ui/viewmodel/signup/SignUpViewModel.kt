package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.viewmodel.EffectChannel
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

    private val effectChannel = EffectChannel<SignUpEffect>()
    val effects = effectChannel.effects

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.Submit -> signUp(action.name, action.email, action.password)
        }
    }

    private fun signUp(
        name: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            _state.value = SignUpUIState.Loading

            sessionManager.logout()

            authRepository
                .register(name, email, password)
                .catch { effectChannel.send(SignUpEffect.ShowError(it.toAppError())) }
                .collect { effectChannel.send(SignUpEffect.NavigateToHome) }

            _state.value = SignUpUIState.Idle
        }
    }
}
