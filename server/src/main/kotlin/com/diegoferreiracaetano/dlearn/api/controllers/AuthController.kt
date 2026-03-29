package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.exception.challengePreference
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRequest
import com.diegoferreiracaetano.dlearn.domain.auth.CreateUserRequest
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType.*
import com.diegoferreiracaetano.dlearn.orchestrator.auth.CreateUserOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.LoginOrchestrator
import io.ktor.http.HttpHeaders.AcceptLanguage
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.*
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Route.authController() {
    val loginOrchestrator by inject<LoginOrchestrator>()
    val createUserOrchestrator by inject<CreateUserOrchestrator>()
    val logger = LoggerFactory.getLogger("AuthController")

    route("/v1/auth") {
        post("/login") {
            val request = call.receive<AuthRequest>()
            val language = call.request.header(AcceptLanguage) ?: "en"
            
            logger.info("Login request received for ${request.email} with metadata: ${request.metadata.keys}")

            val response = loginOrchestrator.login(
                email = request.email,
                password = request.password,
                metadata = request.metadata,
                language = language
            )
            call.respond(response)
        }

        challengePreference(OTP_EMAIL) {
            post("/register") {
                val request = try {
                    call.receive<CreateUserRequest>()
                } catch (e: Exception) {
                    logger.error("Failed to receive CreateUserRequest", e)
                    throw BadRequestException("Invalid request body")
                }
                
                val language = call.request.header(AcceptLanguage) ?: "en"
                
                logger.info("Register request received for ${request.email}. Metadata size: ${request.metadata.size}. Keys: ${request.metadata.keys}")

                val response = createUserOrchestrator.create(
                    name = request.name,
                    email = request.email,
                    password = request.password,
                    metadata = request.metadata,
                    language = language
                )
                call.respond(HttpStatusCode.Created, response)
            }
        }

        post("/refresh") {
            val params = call.receive<Map<String, String>>()
            val refreshToken = params["refresh_token"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val language = call.request.header(AcceptLanguage) ?: "en"

            val response = loginOrchestrator.refreshToken(refreshToken, language)
            call.respond(response)
        }

        post("/logout") {
            call.respond(HttpStatusCode.OK)
        }
    }
}
