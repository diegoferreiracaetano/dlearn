package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlinx.serialization.Serializable

@Serializable
data class ResolveChallengeBody(
    val type: ChallengeType,
    val answers: Map<String, String>
)

@Serializable
data class ChallengeResponse(
    val success: Boolean,
    val message: String? = null,
    val validatedToken: String? = null
)

fun Route.challengeController() {
    val challengeDataService by inject<ChallengeDataService>()

    route("/v1/auth/challenge") {
        
        post("/resolve") {
            val transactionId = call.request.headers["X-Transaction-Id"]
            if (transactionId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, ChallengeResponse(success = false, message = "Missing X-Transaction-Id header"))
                return@post
            }

            val request = call.receive<ResolveChallengeBody>()
            
            val validatedToken = challengeDataService.resolveChallenge(
                transactionId = transactionId,
                answers = request.answers
            )

            if (validatedToken != null) {
                call.respond(
                    HttpStatusCode.OK, 
                    ChallengeResponse(success = true, validatedToken = validatedToken)
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, 
                    ChallengeResponse(success = false, message = "Invalid challenge answer")
                )
            }
        }

        post("/resend") {
            val transactionId = call.request.headers["X-Transaction-Id"]
            if (transactionId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, ChallengeResponse(success = false, message = "Missing X-Transaction-Id header"))
                return@post
            }
            
            val success = challengeDataService.resendChallenge(transactionId)

            if (success) {
                call.respond(
                    HttpStatusCode.OK, 
                    ChallengeResponse(success = true, message = "Challenge resent successfully")
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound, 
                    ChallengeResponse(success = false, message = "Transaction not found")
                )
            }
        }
    }
}
