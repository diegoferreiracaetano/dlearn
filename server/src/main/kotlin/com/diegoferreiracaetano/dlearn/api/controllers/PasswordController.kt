package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.VerifyOtpRequest
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordOrchestrator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("PasswordController")

fun Route.passwordController() {
    val orchestrator by inject<PasswordOrchestrator>()

    route("/v1/password") {
        post("/change") {
            val request = call.receive<ChangePasswordRequest>()
            val challengeToken = call.request.headers[SecurityConstants.HEADER_CHALLENGE_TOKEN]
            val lang = call.request.acceptLanguage() ?: AppConstants.DEFAULT_LANG

            logger.info("Change password request for user: ${request.userId} with token: $challengeToken")

            orchestrator.changePassword(request, challengeToken, lang)
                .collect { response ->
                    call.respond(HttpStatusCode.OK, response)
                }
        }

        post("/verify-otp") {
            val request = call.receive<VerifyOtpRequest>()
            val challengeToken = call.request.headers[SecurityConstants.HEADER_CHALLENGE_TOKEN]
            val lang = call.request.acceptLanguage() ?: AppConstants.DEFAULT_LANG

            logger.info("Verify OTP request: code=${request.otpCode}, token=$challengeToken")

            orchestrator.verifyOtp(request, challengeToken, lang)
                .collect { response ->
                    if (response.success) {
                        logger.info("OTP verified successfully for token: $challengeToken")
                        call.respond(HttpStatusCode.OK, response)
                    } else {
                        logger.warn("OTP verification failed for token: $challengeToken - code=${request.otpCode}")
                        call.respond(HttpStatusCode.BadRequest, response)
                    }
                }
        }
    }
}
