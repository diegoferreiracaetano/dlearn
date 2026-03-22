package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.exception.challengePreference
import com.diegoferreiracaetano.dlearn.api.exception.challengeToken
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.models.*
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordOrchestrator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.passwordController() {
    val orchestrator by inject<PasswordOrchestrator>()

    route("/v1/password") {
        
        /**
         * Rota protegida por desafio genérico via DSL.
         * O parâmetro 'challengeToken' na lambda é o provedor seguro.
         */
      challengePreference(ChallengeType.OTP_EMAIL) {
            post("/change") {
                val request = call.receive<ChangePasswordRequest>()

                orchestrator.changePassword(
                    request = request,
                    challengeToken = call.challengeToken
                ).collect { response ->
                    call.respond(HttpStatusCode.OK, response)
                }
            }
      }
    }
}
