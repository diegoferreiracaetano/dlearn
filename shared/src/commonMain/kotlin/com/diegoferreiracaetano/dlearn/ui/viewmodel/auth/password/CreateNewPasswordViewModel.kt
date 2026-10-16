package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.ui.viewmodel.EffectChannel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CreateNewPasswordViewModel(
    private val passwordRepository: PasswordRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateNewPasswordUiState>(CreateNewPasswordUiState.Idle)
    val uiState: StateFlow<CreateNewPasswordUiState> = _uiState.asStateFlow()

    private val effectChannel = EffectChannel<CreateNewPasswordEffect>()
    val effects = effectChannel.effects

    fun onAction(action: CreateNewPasswordAction) {
        when (action) {
            is CreateNewPasswordAction.ChangePassword -> changePassword(action.newPassword)
        }
    }

    private fun changePassword(newPassword: String) {
        viewModelScope.launch {
            _uiState.value = CreateNewPasswordUiState.Loading

            passwordRepository
                .changePassword(newPassword)
                .catch { effectChannel.send(CreateNewPasswordEffect.ShowError(it.toAppError())) }
                .collect { effectChannel.send(CreateNewPasswordEffect.Changed(it.message)) }

            _uiState.value = CreateNewPasswordUiState.Idle
        }
    }
}
