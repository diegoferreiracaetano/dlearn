package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val challengeRequired: Boolean = false,
    val screen: Screen? = null
)

class LoginOrchestrator(
    private val tokenService: TokenService
) {

    fun login(email: String, password: String): AuthResponse {
        // Simulação de validação (Em produção buscaria no banco de dados)
        if (email == "admin@dlearn.com" && password == "123456") {
            val user = User(
                id = "1",
                name = "Diego Caetano",
                email = email
            )

            // Lógica de "Trust Device" - Padrão Bancário
            // Se fosse um dispositivo novo, retornaríamos challengeRequired = true
            
            return AuthResponse(
                user = user,
                accessToken = tokenService.generateAccessToken(user),
                refreshToken = tokenService.generateRefreshToken(user)
            )
        }
        
        return AuthResponse(challengeRequired = false)
    }

    fun refreshToken(token: String): AuthResponse {
        val userId = tokenService.verifyToken(token) ?: return AuthResponse()
        
        // Simulação de busca de usuário após validação do token
        val user = User(id = userId, name = "Diego Caetano", email = "admin@dlearn.com")
        
        return AuthResponse(
            accessToken = tokenService.generateAccessToken(user),
            refreshToken = tokenService.generateRefreshToken(user) // Rotação de Refresh Token
        )
    }
}
