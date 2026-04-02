package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import kotlinx.serialization.Serializable

/**
 * Mapeamento exaustivo de tipos de desafios (MFA/2FA) comuns no mercado.
 */
@Serializable
enum class ChallengeType {
    OTP_SMS, // Código via SMS
    OTP_EMAIL, // Código via E-mail
    BIOMETRIC, // Digital, FaceID, etc.
    PASSWORD, // Confirmação de senha atual
    TOTP, // Time-based One-Time Password (Google Authenticator, Authy)
    PUSH_NOTIFICATION, // Aprovação em dispositivo confiável
    KBA, // Knowledge-Based Authentication (Perguntas de segurança)
    DEVICE_BINDING, // Verificação silenciosa de dispositivo vinculado
    UNKNOWN,
}

/**
 * Representa um desafio específico enviado pelo servidor.
 */
@Serializable
data class Challenge(
    val challengeType: ChallengeType,
    /** Dados adicionais para o desafio (ex: final do celular, e-mail mascarado) */
    val data: Map<String, String> = emptyMap(),
)

/**
 * Sessão de desafio que contém o desafio específico selecionado pelo servidor.
 */
@Serializable
data class ChallengeSession(
    val transactionId: String,
    val challenge: Challenge,
    val expiresAt: Long? = null,
)

/**
 * Resultado da tentativa de resolução de um desafio.
 */
sealed interface ChallengeResult {
    /** Sucesso: contém os headers/tokens de validação para o retry */
    data class Success(
        val data: Map<String, String>,
    ) : ChallengeResult

    /** Usuário cancelou o fluxo */
    data object Cancelled : ChallengeResult

    /** Falha na validação ou erro técnico */
    data class Failure(
        val error: Throwable,
    ) : ChallengeResult
}

/**
 * Exceção lançada quando um desafio é cancelado pelo usuário.
 */
class ChallengeCancelledException : Exception("O desafio de segurança foi cancelado.")
