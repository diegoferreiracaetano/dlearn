package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CreateNewPasswordViewModel(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState>(
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Idle
    }

    fun changePassword(userId: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Loading
            passwordRepository.changePassword(userId, newPassword)
                .onStart {
                    _uiState.value = _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Loading
                }
                .catch { error ->
                    _uiState.value = _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Error(error.message.toString())
                }
                .collect { response ->
                    _uiState.value = _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Success(response.message)
                }
        }
    }
}
