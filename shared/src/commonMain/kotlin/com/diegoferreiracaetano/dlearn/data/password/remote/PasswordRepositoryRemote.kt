package com.diegoferreiracaetano.dlearn.data.password.remote

import com.diegoferreiracaetano.dlearn.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.password.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordRepositoryRemote(
    private val httpClient: HttpClient,
    private val challengeCoordinator: ChallengeCoordinator
) : PasswordRepository {
    
    override fun changePassword(userId: String, newPassword: String): Flow<ChangePasswordResponse> = flow {
        val request = ChangePasswordRequest(userId, newPassword)
        val response = httpClient.post("v1/password/change") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ChangePasswordResponse>()
        emit(response)
    }

    override fun verifyOtp(request: VerifyOtpRequest): Flow<VerifyOtpResponse> = flow {
        // Busca o token do desafio ativo de forma transparente
        val activeToken = challengeCoordinator.activeRequest.value?.session?.transactionId

        val response = httpClient.post("v1/password/verify-otp") {
            contentType(ContentType.Application.Json)
            // Injeta o header de segurança necessário para o servidor validar o desafio
            activeToken?.let { header(SecurityConstants.HEADER_CHALLENGE_TOKEN, it) }
            setBody(request)
        }.body<VerifyOtpResponse>()

        // Se o OTP deu sucesso e temos um desafio ativo, resolvemos ele para liberar o Interceptor
        if (response.success && activeToken != null) {
            val resultData = mapOf(SecurityConstants.HEADER_CHALLENGE_TOKEN to "validated-$activeToken")
            challengeCoordinator.resolve(ChallengeResult.Success(resultData))
        }

        emit(response)
    }
}
