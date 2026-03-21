package com.diegoferreiracaetano.dlearn.ui.screens.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.ui.screens.auth.password.state.CreateNewPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CreateNewPasswordViewModel(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateNewPasswordUiState>(CreateNewPasswordUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun changePassword(userId: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = CreateNewPasswordUiState.Loading
            passwordRepository.changePassword(userId, newPassword)
                .onStart {
                    _uiState.value = CreateNewPasswordUiState.Loading
                }
                .catch { error ->
                    _uiState.value = CreateNewPasswordUiState.Error(error.message.toString())
                }
                .collect { response ->
                    _uiState.value = CreateNewPasswordUiState.Success(response.message)
                }
        }
    }
}
