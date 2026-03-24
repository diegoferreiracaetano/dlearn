package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService

class LoginOrchestrator(
    private val tokenService: TokenService
) {

    suspend fun login(email: String, password: String) : AuthResponse {
        // Simulação de validação
        if (email == "admin@dlearn.com" && password == "123456") {
            val user = User(
                id = "1",
                name = "Diego Caetano",
                email = email
            )

            return AuthResponse(
                user = user,
                accessToken = tokenService.generateAccessToken(user),
                refreshToken = tokenService.generateRefreshToken(user),
                challengeRequired = false
            )
        } else {
            throw SecurityException("Usuário não encontrado ou senha inválida")
        }
    }

    suspend fun refreshToken(token: String): AuthResponse {
        val userId = tokenService.verifyToken(token) ?: throw SecurityException("Sessão inválida")

        val user = User(id = userId, name = "Diego Caetano", email = "admin@dlearn.com")

        return AuthResponse(
            accessToken = tokenService.generateAccessToken(user),
            refreshToken = tokenService.generateRefreshToken(user),
            challengeRequired = false
        )
    }
}