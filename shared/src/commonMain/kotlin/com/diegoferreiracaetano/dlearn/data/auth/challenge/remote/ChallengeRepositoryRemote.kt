package com.diegoferreiracaetano.dlearn.data.auth.challenge.remote

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementação remota unificada para qualquer tipo de desafio.
 */
class ChallengeRepositoryRemote(
    private val httpClient: HttpClient
) : ChallengeRepository {

    override fun resolveChallenge(
        transactionId: String,
        type: ChallengeType,
        answer: Map<String, String>
    ): Flow<ChallengeResult> = flow {
        try {
            /**
             * Mapeamento de endpoints:
             * Se for verificação de conta, podemos usar uma rota específica ou 
             * uma rota genérica de MFA dependendo do backend.
             */
            val endpoint = when(type) {
                ChallengeType.OTP_EMAIL -> "v1/auth/verify-otp" // Rota unificada de verificação
                else -> "v1/auth/challenge/resolve"
            }

            val response = httpClient.post(endpoint) {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "transactionId" to transactionId,
                    "type" to type.name,
                    "answers" to answer
                ))
            }
            
            if (response.status.value in 200..299) {
                emit(ChallengeResult.Success(answer))
            } else {
                emit(ChallengeResult.Failure(Exception("Erro ao validar desafio")))
            }
        } catch (e: Exception) {
            emit(ChallengeResult.Failure(e))
        }
    }

    override fun resendChallenge(transactionId: String, type: ChallengeType): Flow<Boolean> = flow {
        try {
            httpClient.post("v1/auth/challenge/resend") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("transactionId" to transactionId, "type" to type.name))
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}
