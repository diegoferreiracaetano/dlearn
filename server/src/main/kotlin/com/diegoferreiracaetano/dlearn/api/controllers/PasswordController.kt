package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.VerifyOtpRequest
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordChallengeException
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordOrchestrator
import com.diegoferreiracaetano.dlearn.util.PreconditionRequired
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.passwordController() {
    val orchestrator by inject<PasswordOrchestrator>()

    route("/v1/password") {
        post("/change") {
            val request = call.receive<ChangePasswordRequest>()
            val challengeToken = call.request.headers[SecurityConstants.HEADER_CHALLENGE_TOKEN]
            val lang = call.request.acceptLanguage() ?: AppConstants.DEFAULT_LANG

            val result = orchestrator.changePassword(request, challengeToken, lang)

            result.fold(
                onSuccess = { response ->
                    call.respond(HttpStatusCode.OK, response)
                },
                onFailure = { error ->
                    if (error is PasswordChallengeException) {
                        call.respond(HttpStatusCode.PreconditionRequired, error.error)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to (error.message ?: "Unknown error")))
                    }
                }
            )
        }

        post("/verify-otp") {
            val request = call.receive<VerifyOtpRequest>()
            val challengeToken = call.request.headers[SecurityConstants.HEADER_CHALLENGE_TOKEN]
            val lang = call.request.acceptLanguage() ?: AppConstants.DEFAULT_LANG

            val response = orchestrator.verifyOtp(request, challengeToken, lang)
            
            if (response.success) {
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.BadRequest, response)
            }
        }
    }
}
