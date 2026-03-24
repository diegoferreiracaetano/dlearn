package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String
)

val ChallengePreferenceKey = AttributeKey<ChallengeType>("ChallengePreference")

fun Application.configureStatusPages() {

    val challengeMapper by inject<ChallengeMapper>()

    install(StatusPages) {

        // Captura SecurityException especificamente para garantir 401 Unauthorized
        exception<SecurityException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = ErrorResponse(
                    code = "UNAUTHORIZED",
                    message = cause.message ?: "Credenciais inválidas"
                )
            )
        }

        exception<Throwable> { call, cause ->
            val preferenceFromController = call.attributes.getOrNull(ChallengePreferenceKey)
            val preferenceHeader = call.request.header(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)

            val preferredType = preferenceFromController
                    ?: preferenceHeader?.let { header ->
                        ChallengeType.entries.find { it.name == header }
                    }

            val challenge = challengeMapper.toChallengeSession(cause, preferredType)

            if (challenge != null) {
                // 428 Precondition Required para fluxos de Challenge (MFA)
                call.respond(HttpStatusCode.fromValue(428), challenge)
            } else {
                val status = when (cause) {
                    is IllegalArgumentException -> HttpStatusCode.BadRequest
                    is SecurityException -> HttpStatusCode.Unauthorized
                    else -> HttpStatusCode.InternalServerError
                }

                call.respond(
                    status = status,
                    message = ErrorResponse(
                        code = cause::class.simpleName ?: "ERROR",
                        message = cause.message ?: "Erro interno no servidor"
                    )
                )
            }
        }
    }
}