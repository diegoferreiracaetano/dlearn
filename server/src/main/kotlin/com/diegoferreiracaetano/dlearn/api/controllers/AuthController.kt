package com.diegoferreiracaetano.dlearn.api.controllers

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
            val params = call.receive<Map<String, String>>()
            val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val language = call.request.header(AcceptLanguage) ?: "en"

            val response = loginOrchestrator.login(email, password, language)
            call.respond(response)
        }

        post("/register") {
            val params = call.receive<Map<String, String>>()
            val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val language = call.request.header(AcceptLanguage) ?: "en"

            val response = createUserOrchestrator.createUser(name, email, password, language)
            call.respond(HttpStatusCode.Created, response)
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
