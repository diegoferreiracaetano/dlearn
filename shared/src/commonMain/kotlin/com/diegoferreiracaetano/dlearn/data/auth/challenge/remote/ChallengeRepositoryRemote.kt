package com.diegoferreiracaetano.dlearn.data.auth.challenge.remote

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
private data class ResolveChallengeRequest(
    val type: String,
    val answers: String
)

@Serializable
private data class ResendChallengeRequest(
    val type: String
)

@Serializable
private data class ChallengeBackendResponse(
    val success: Boolean,
    val message: String? = null,
    val validatedToken: String? = null
)

/**
 * Implementação remota unificada para qualquer tipo de desafio.
 * Recupera o contexto (ID e Tipo) do ChallengeCoordinator interno.
 */
class ChallengeRepositoryRemote(
    private val httpClient: HttpClient,
    private val coordinator: ChallengeCoordinator,
    private val otpHandler: OtpChallengeHandler
) : ChallengeRepository {

    override fun resolveChallenge(answer: String): Flow<ChallengeResult> = flow {
        val session = coordinator.currentSession ?: throw Exception("Nenhuma sessão de desafio ativa")
        val challenge = coordinator.activeChallenge ?: throw Exception("Desafio ativo não encontrado")

        try {
            val response = httpClient.post("v1/auth/challenge/resolve") {
                contentType(ContentType.Application.Json)
                header(SecurityConstants.HEADER_TRANSACTION_ID, session.transactionId)
                setBody(ResolveChallengeRequest(
                    type = challenge.challengeType.name,
                    answers = answer
                ))
            }

            if (response.status.value in 200..299) {
                val body = response.body<ChallengeBackendResponse>()
                val validatedToken = body.validatedToken ?: ""

                if (challenge.challengeType == ChallengeType.OTP_EMAIL || challenge.challengeType == ChallengeType.OTP_SMS) {
                    otpHandler.onChallengeResolved(validatedToken)
                }

                coordinator.clear() // Limpa contexto após sucesso
                emit(ChallengeResult.Success(mapOf("validatedToken" to validatedToken)))
            } else {
                emit(ChallengeResult.Failure(Exception("Erro ao validar desafio: ${response.status}")))
            }
        } catch (e: ClientRequestException) {

            val message = try {
                val errorBody = e.response.body<ChallengeBackendResponse>()
                errorBody.message
            } catch (_: Exception) {
                try {
                    e.response.bodyAsText()
                } catch (_: Exception) {
                    "Erro desconhecido ao validar desafio"
                }
            }

            emit(
                ChallengeResult.Failure(
                    Throwable(message)
                )
            )
        }
    }

    override fun resendChallenge(): Flow<Boolean> = flow {
        val session = coordinator.currentSession ?: return@flow emit(false)
        val challenge = coordinator.activeChallenge ?: return@flow emit(false)

        try {
            val response = httpClient.post("v1/auth/challenge/resend") {
                contentType(ContentType.Application.Json)
                header(SecurityConstants.HEADER_TRANSACTION_ID, session.transactionId)
                setBody(ResendChallengeRequest(type = challenge.challengeType.name))
            }
            emit(response.status.value in 200..299)
        } catch (e: Exception) {
            emit(false)
        }
    }
}
