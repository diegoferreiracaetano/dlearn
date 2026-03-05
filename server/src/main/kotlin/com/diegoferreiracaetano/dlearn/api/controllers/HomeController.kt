package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.HomeOrchestrator
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.homeController(orchestrator: HomeOrchestrator) {
    route("/v1/home") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            
            val screen = orchestrator.getHomeData(userId, appVersion)
            call.respond(screen)
        }
    }
}
