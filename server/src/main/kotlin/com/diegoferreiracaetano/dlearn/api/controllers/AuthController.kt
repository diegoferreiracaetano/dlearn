package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.LoginOrchestrator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.authController() {
    val orchestrator by inject<LoginOrchestrator>()

    route("/auth") {
        post("/login") {
            val params = call.receive<Map<String, String>>()
            val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

            val response = orchestrator.login(email, password)
            call.respond(response)
        }

        post("/refresh") {
            val params = call.receive<Map<String, String>>()
            val refreshToken = params["refresh_token"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            
            val response = orchestrator.refreshToken(refreshToken)
            call.respond(response)
        }

        post("/logout") {
            call.respond(HttpStatusCode.OK)
        }
    }
}
