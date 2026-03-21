package com.diegoferreiracaetano.dlearn.domain.password

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.challenge.ResolveChallengeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * UseCase para verificar o código OTP.
 * Agora utiliza o ChallengeRepository através do fluxo genérico resolveChallenge.
 * Mantido para compatibilidade enquanto o App migra para o ResolveChallengeRequest genérico.
 */
class VerifyOtpUseCase(
    private val challengeRepository: ChallengeRepository,
    private val otpHandler: OtpChallengeHandler
) {
    operator fun invoke(userId: String, otpCode: String): Flow<ResolveChallengeResponse> =
        challengeRepository.resolveChallenge(
            transactionId = userId,
            type = ChallengeType.OTP_EMAIL,
            answer = mapOf("otp" to otpCode)
        ).onEach { result ->
            if (result is ChallengeResult.Success) {
                val token = result.data["X-Challenge-Token"]
                if (token != null) {
                    otpHandler.onChallengeResolved(token)
                }
            }
        }.map { result ->
            when (result) {
                is ChallengeResult.Success -> ResolveChallengeResponse(
                    success = true,
                    message = "Sucesso",
                    validatedToken = result.data["X-Challenge-Token"]
                )
                is ChallengeResult.Failure -> ResolveChallengeResponse(
                    success = false,
                    message = result.error.message ?: "Erro na verificação"
                )
                is ChallengeResult.Cancelled -> ResolveChallengeResponse(
                    success = false,
                    message = "Cancelado pelo usuário"
                )
            }
        }
}
