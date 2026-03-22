package com.diegoferreiracaetano.dlearn.ui.screens.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.state.VerifyAccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela de verificação de conta.
 * Responsabilidade: Coletar o código do usuário e enviar ao backend via ChallengeRepository.
 */
class VerifyAccountViewModel(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VerifyAccountUiState>(VerifyAccountUiState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Envia o código OTP para validação no backend.
     * O answer aqui é apenas a String do código digitado.
     */
    fun verifyOtp(userId: String, otpCode: String) {
        viewModelScope.launch {
            challengeRepository.resolveChallenge(
                transactionId = userId,
                type = ChallengeType.OTP_EMAIL,
                answer = otpCode // Passando apenas a String
            )
            .onStart {
                _uiState.value = VerifyAccountUiState.Loading
            }
            .catch { error ->
                _uiState.value = VerifyAccountUiState.Error(error.message.toString())
            }
            .collect { result ->
                when (result) {
                    is ChallengeResult.Success -> {
                        _uiState.value = VerifyAccountUiState.Success
                    }
                    is ChallengeResult.Failure -> {
                        _uiState.value = VerifyAccountUiState.Error(result.error.message ?: "Erro na verificação")
                    }
                    is ChallengeResult.Cancelled -> {
                        _uiState.value = VerifyAccountUiState.Idle
                    }
                }
            }
        }
    }

    /**
     * Solicita o reenvio do desafio ao backend.
     */
    fun resendOtp(userId: String) {
        viewModelScope.launch {
            challengeRepository.resendChallenge(
                transactionId = userId,
                type = ChallengeType.OTP_EMAIL
            )
            .onStart {
                _uiState.value = VerifyAccountUiState.Loading
            }
            .catch { error ->
                _uiState.value = VerifyAccountUiState.Error(error.message.toString())
            }
            .collect { success ->
                if (success) {
                    _uiState.value = VerifyAccountUiState.Idle
                } else {
                    _uiState.value = VerifyAccountUiState.Error("Falha ao reenviar código")
                }
            }
        }
    }
}
