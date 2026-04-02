package com.diegoferreiracaetano.dlearn.domain.password

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.challenge.ResolveChallengeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * UseCase para verificar o código OTP.
 * Agora utiliza o ChallengeRepository através do fluxo genérico resolveChallenge.
 * O contexto de ID e Tipo é mantido internamente pelo repositório/coordenador.
 */
class VerifyOtpUseCase(
    private val challengeRepository: ChallengeRepository,
) {
    operator fun invoke(otpCode: String): Flow<ResolveChallengeResponse> =
        challengeRepository
            .resolveChallenge(
                answer = otpCode,
            ).map { result ->
                when (result) {
                    is ChallengeResult.Success ->
                        ResolveChallengeResponse(
                            success = true,
                            message = "Sucesso",
                            validatedToken = result.data["validatedToken"],
                        )
                    is ChallengeResult.Failure ->
                        ResolveChallengeResponse(
                            success = false,
                            message = result.error.message ?: "Erro na verificação",
                        )
                    is ChallengeResult.Cancelled ->
                        ResolveChallengeResponse(
                            success = false,
                            message = "Cancelado pelo usuário",
                        )
                }
            }
}
