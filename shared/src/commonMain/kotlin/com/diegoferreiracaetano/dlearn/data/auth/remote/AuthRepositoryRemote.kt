package com.diegoferreiracaetano.dlearn.data.auth.remote

import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryRemote(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun login(email: String, password: String): Flow<AuthResponse> = flow {
        // expectSuccess = true no SharedModule garante que isso lance exceção em caso de 401/500
        val response = httpClient.post("/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email, "password" to password))
        }.body<AuthResponse>()
        
        if (response.accessToken != null && response.refreshToken != null && response.user != null) {
            sessionManager.login(
                user = response.user,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        }

        emit(response)
    }

    override suspend fun refreshToken(refreshToken: String): AuthResponse {
        return httpClient.post("/v1/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to refreshToken))
        }.body<AuthResponse>()
    }

    override suspend fun logout() {
        httpClient.post("/v1/auth/logout")
        sessionManager.logout()
    }
}