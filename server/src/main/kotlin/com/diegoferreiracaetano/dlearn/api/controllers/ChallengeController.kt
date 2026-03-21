package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.challenge.ResolveChallengeRequest
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.challengeController() {
    val challengeDataService by inject<ChallengeDataService>()

    route("/v1/auth/challenge") {
        
        post("/resolve") {
            val request = call.receive<ResolveChallengeRequest>()
            
            // Valida a resposta do desafio usando o serviço especializado
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
            call.respond(HttpStatusCode.OK)
        }
    }
}
