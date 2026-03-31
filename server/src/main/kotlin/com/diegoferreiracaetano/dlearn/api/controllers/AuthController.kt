package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.exception.challengePreference
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRequest
import com.diegoferreiracaetano.dlearn.domain.auth.CreateUserRequest
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType.OTP_EMAIL
import com.diegoferreiracaetano.dlearn.orchestrator.auth.CreateUserOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.LoginOrchestrator
import io.ktor.http.HttpHeaders.AcceptLanguage
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.authController() {
    val loginOrchestrator by inject<LoginOrchestrator>()
    val createUserOrchestrator by inject<CreateUserOrchestrator>()

    route("/v1/auth") {
        post("/login") {
            val request = call.receive<AuthRequest>()
            val language = call.request.header(AcceptLanguage) ?: "en"
            
            val response = loginOrchestrator.login(
                email = request.email,
                password = request.password,
                language = language
            )

            call.respond(response)
        }

        post("/social-login") {
            val params = call.receive<Map<String, String?>>()
            val provider = params["provider"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val idToken = params["id_token"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val accessToken = params["access_token"]
            val language = call.request.header(AcceptLanguage) ?: "en"

            val response = loginOrchestrator.socialLogin(
                provider = provider,
                idToken = idToken,
                accessToken = accessToken,
                language = language
            )

            call.respond(response)
        }

        challengePreference(OTP_EMAIL) {
            post("/register") {
                val request = call.receive<CreateUserRequest>()
                val language = call.request.header(AcceptLanguage) ?: "en"
                
                val response = createUserOrchestrator.create(
                    name = request.name,
                    email = request.email,
                    password = request.password,
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
