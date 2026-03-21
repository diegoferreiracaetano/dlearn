package com.diegoferreiracaetano.dlearn.ui.screens.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.domain.password.VerifyOtpRequest
import com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.state.VerifyAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VerifyAccountViewModel(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VerifyAccountUiState>(VerifyAccountUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun verifyOtp(userId: String, otpCode: String) {
        viewModelScope.launch {
            passwordRepository.verifyOtp(VerifyOtpRequest(userId, otpCode))
                .onStart {
                    _uiState.value = VerifyAccountUiState.Loading
                }
                .catch { error ->
                    _uiState.value = VerifyAccountUiState.Error(error.message.toString())
                }
                .collect { response ->
                    if (response.success) {
                        _uiState.value = VerifyAccountUiState.Success
                    } else {
                        _uiState.value = VerifyAccountUiState.Error(response.message)
                    }
                }
        }
    }
}
