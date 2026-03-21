package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.domain.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordChallengeException
import com.diegoferreiracaetano.dlearn.util.PreconditionRequired
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

class GlobalExceptionHandler {
    suspend fun handle(call: ApplicationCall, cause: Throwable) {
        when (cause) {
            is PasswordChallengeException -> {
                val challengeSession = ChallengeSession(
                    transactionId = cause.error.challengeToken,
                    challenges = listOf(
                        Challenge(
                            challengeType = ChallengeType.OTP_EMAIL,
                            data = mapOf("message" to cause.error.message)
                        )
                    )
                )
                call.respond(HttpStatusCode.PreconditionRequired, challengeSession)
            }
            else -> {
                call.respondText(
                    text = "Internal Server Error: ${cause.localizedMessage ?: "Unknown error"}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}
