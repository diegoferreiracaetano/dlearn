package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.challenge.ResolveChallengeRequest
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.challengeController() {
    val passwordDataService by inject<PasswordDataService>()

    route("/v1/auth/challenge") {
        
        post("/resolve") {
            val request = call.receive<ResolveChallengeRequest>()
            
            // Valida a resposta do desafio (ex: confere o OTP)
            val validatedToken = passwordDataService.resolveChallenge(
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
            // Lógica de reenvio
            call.respond(HttpStatusCode.OK)
        }
    }
}
