package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VerifyAccountViewModel(
    private val challengeRepository: ChallengeRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<VerifyAccountUiState>(
            VerifyAccountUiState.Idle,
        )
    val uiState = _uiState.asStateFlow()

    fun verifyOtp(otpCode: String) {
        viewModelScope.launch {
            challengeRepository
                .resolveChallenge(
                    answer = otpCode,
                ).onStart {
                    _uiState.value = VerifyAccountUiState.Loading
                }.catch { error ->
                    _uiState.value = VerifyAccountUiState.Error(error)
                }.collect { result ->
                    _uiState.value = result.toUiState()
                }
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            challengeRepository
                .resendChallenge()
                .onStart {
                    _uiState.value = VerifyAccountUiState.Loading
                }.catch { error ->
                    _uiState.value = VerifyAccountUiState.Error(error)
                }.collect { success ->
                    if (success) {
                        _uiState.value = VerifyAccountUiState.Idle
                    } else {
                        _uiState.value = VerifyAccountUiState.Error(Throwable("Failed to resend OTP"))
                    }
                }
        }
    }

    fun cancel() {
        challengeRepository.cancelChallenge()
    }
}
