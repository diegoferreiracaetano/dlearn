package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.challenge.ResolveChallengeRequest
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlinx.serialization.Serializable

@Serializable
data class ResendChallengeRequest(val transactionId: String)

fun Route.challengeController() {
    val challengeDataService by inject<ChallengeDataService>()

    route("/v1/auth/challenge") {
        
        post("/resolve") {
            val request = call.receive<ResolveChallengeRequest>()
            
            val validatedToken = challengeDataService.resolveChallenge(
                transactionId = request.transactionId,
                answers = request.answers
            )

            if (validatedToken != null) {
                call.respond(HttpStatusCode.OK, mapOf("validatedToken" to validatedToken, "success" to true))
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf("success" to false, "message" to "Invalid challenge answer"))
            }
        }

        post("/resend") {
            val request = call.receive<ResendChallengeRequest>()
            
            val success = challengeDataService.resendChallenge(request.transactionId)

            if (success) {
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "message" to "Challenge resent successfully"))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("success" to false, "message" to "Transaction not found"))
            }
        }
    }
}
