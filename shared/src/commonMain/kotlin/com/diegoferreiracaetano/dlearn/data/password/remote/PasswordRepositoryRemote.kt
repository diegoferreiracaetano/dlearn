package com.diegoferreiracaetano.dlearn.data.password.remote

import com.diegoferreiracaetano.dlearn.auth.network.challengePreference
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositório remoto para operações de senha.
 * 
 * Implementação limpa que foca apenas no domínio de Password.
 * A resolução de desafios (MFA) é tratada de forma transparente pelo Interceptor.
 */
class PasswordRepositoryRemote(
    private val httpClient: HttpClient
) : PasswordRepository {
    
    override fun changePassword(userId: String, newPassword: String): Flow<ChangePasswordResponse> = flow {
        val request = ChangePasswordRequest(userId, newPassword)
        
        val response = httpClient.post("v1/password/change") {
            contentType(ContentType.Application.Json)
            setBody(request)
            
            /** 
             * APLICAÇÃO DO FILTRO DE PREFERÊNCIA: 
             * Informamos ao servidor que para esta operação de troca de senha, 
             * preferimos o desafio do tipo OTP_EMAIL.
             */
            challengePreference(ChallengeType.OTP_EMAIL)
            
        }.body<ChangePasswordResponse>()
        
        emit(response)
    }
}
