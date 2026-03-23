package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.app.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.homeController(orchestrator: HomeOrchestrator) {
    route("/v1/home") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgent = call.request.header(UserAgent) ?: ""
            val type = call.request.queryParameters["type"] ?: "ALL"

            val request = AppRequest(
                path = "/home",
                params = mapOf("type" to type)
            )

            orchestrator.execute(
                request = request,
                userId = userId,
                userAgent = userAgent
            ).collect { screen ->
                call.respond(screen)
            }
        }
    }
}
