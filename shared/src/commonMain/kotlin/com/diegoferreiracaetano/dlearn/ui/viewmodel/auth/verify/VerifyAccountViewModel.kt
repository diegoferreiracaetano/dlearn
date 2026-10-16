package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.ui.viewmodel.EffectChannel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VerifyAccountViewModel(
    private val challengeRepository: ChallengeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<VerifyAccountUiState>(VerifyAccountUiState.Idle)
    val uiState: StateFlow<VerifyAccountUiState> = _uiState.asStateFlow()

    private val effectChannel = EffectChannel<VerifyAccountEffect>()
    val effects = effectChannel.effects

    fun onAction(action: VerifyAccountAction) {
        when (action) {
            is VerifyAccountAction.VerifyOtp -> verifyOtp(action.code)
            VerifyAccountAction.ResendOtp -> resendOtp()
            VerifyAccountAction.Cancel -> challengeRepository.cancelChallenge()
        }
    }

    private fun verifyOtp(otpCode: String) {
        viewModelScope.launch {
            challengeRepository
                .resolveChallenge(answer = otpCode)
                .onStart { _uiState.value = VerifyAccountUiState.Loading }
                .catch { _uiState.value = VerifyAccountUiState.Error(it.toAppError()) }
                .collect { result ->
                    when (result) {
                        is ChallengeResult.Success -> {
                            _uiState.value = VerifyAccountUiState.Idle
                            effectChannel.send(VerifyAccountEffect.Verified)
                        }

                        is ChallengeResult.Failure ->
                            _uiState.value = VerifyAccountUiState.Error(result.error.toAppError())

                        is ChallengeResult.Cancelled -> _uiState.value = VerifyAccountUiState.Idle
                    }
                }
        }
    }

    private fun resendOtp() {
        viewModelScope.launch {
            challengeRepository
                .resendChallenge()
                .onStart { _uiState.value = VerifyAccountUiState.Loading }
                .catch { _uiState.value = VerifyAccountUiState.Error(it.toAppError()) }
                .collect { success ->
                    _uiState.value =
                        if (success) {
                            VerifyAccountUiState.Idle
                        } else {
                            VerifyAccountUiState.Error(
                                AppError(AppErrorCode.UNKNOWN_ERROR, "Failed to resend OTP"),
                            )
                        }
                }
        }
    }
}
