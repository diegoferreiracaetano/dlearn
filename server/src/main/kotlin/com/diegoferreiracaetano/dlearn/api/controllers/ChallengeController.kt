package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class ResolveChallengeBody(
    val type: String,
    val answers: String,
)

@Serializable
data class ChallengeResponse(
    val success: Boolean,
    val message: String,
    val validatedToken: String? = null,
)

/**
 * Controller responsável por expor os endpoints de resolução de desafios (MFA).
 * No servidor, ele utiliza o ChallengeDataService para validar as respostas.
 */
fun Route.challengeController() {
    val service by inject<ChallengeDataService>()

    route("/v1/auth/challenge") {
        post("/resolve") {
            val transactionId =
                call.request.header(SecurityConstants.HEADER_TRANSACTION_ID)
                    ?: throw AppException(AppError(AppErrorCode.TRANSACTION_ID_REQUIRED))

            val body = call.receive<ResolveChallengeBody>()

            val type =
                ChallengeType.entries.find { it.name == body.type }
                    ?: throw AppException(AppError(AppErrorCode.INVALID_CHALLENGE_CODE))

            val validatedToken =
                service.resolveChallenge(
                    transactionId = transactionId,
                    type = type,
                    answers = mapOf(Constants.OTP_KEY to body.answers),
                )

            if (validatedToken != null) {
                call.respond(
                    HttpStatusCode.OK,
                    ChallengeResponse(
                        success = true,
                        message = "Desafio resolvido",
                        validatedToken = validatedToken,
                    ),
                )
            } else {
                throw AppException(AppError(AppErrorCode.INVALID_CHALLENGE_CODE))
            }
        }

        post("/resend") {
            val transactionId =
                call.request.header(SecurityConstants.HEADER_TRANSACTION_ID)
                    ?: throw AppException(AppError(AppErrorCode.TRANSACTION_ID_REQUIRED))

            val success = service.resendChallenge(transactionId)
            call.respond(HttpStatusCode.OK, mapOf("success" to success))
        }
    }
}
