package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.api.exception.challengePreference
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRequest
import com.diegoferreiracaetano.dlearn.domain.auth.CreateUserRequest
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType.*
import com.diegoferreiracaetano.dlearn.domain.usecases.auth.LinkExternalProviderUseCase
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

fun Route.authController() {
    val loginOrchestrator by inject<LoginOrchestrator>()
    val createUserOrchestrator by inject<CreateUserOrchestrator>()
    val linkExternalProviderUseCase by inject<LinkExternalProviderUseCase>()

    route("/v1/auth") {
        post("/login") {
            val request = call.receive<AuthRequest>()
            val language = call.request.header(AcceptLanguage) ?: "en"
            
            val response = loginOrchestrator.login(
                email = request.email,
                password = request.password,
                language = language
            )

            response.user?.let { user ->
                val metadata = mutableMapOf<String, String>()
                request.tmdbUsername?.let { metadata[MetadataKeys.EXTERNAL_USERNAME] = it }
                request.tmdbPassword?.let { metadata[MetadataKeys.EXTERNAL_PASSWORD] = it }
                
                linkExternalProviderUseCase.execute(userId = user.id, metadata = metadata)
            }

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
                    language = language,
                    tmdbUsername = request.tmdbUsername,
                    tmdbPassword = request.tmdbPassword
                )

                response.user?.let { user ->
                    val metadata = mutableMapOf<String, String>()
                    request.tmdbUsername?.let { metadata[MetadataKeys.EXTERNAL_USERNAME] = it }
                    request.tmdbPassword?.let { metadata[MetadataKeys.EXTERNAL_PASSWORD] = it }
                    
                    linkExternalProviderUseCase.execute(userId = user.id, metadata = metadata)
                }

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
