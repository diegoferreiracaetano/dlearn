package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.app.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.homeController(orchestrator: HomeOrchestrator) {
    route("/v1/home") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            val lang = call.request.acceptLanguage() ?: "en"
            val type = call.request.queryParameters["type"] ?: "ALL"

            val request = AppRequest(
                path = "/home",
                params = mapOf("type" to type)
            )

            orchestrator.execute(
                request = request,
                userId = userId,
                lang = lang,
                appVersion = appVersion
            ).collect { screen ->
                call.respond(screen)
            }
        }
    }
}
