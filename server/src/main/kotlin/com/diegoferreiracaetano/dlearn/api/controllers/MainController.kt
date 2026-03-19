package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.MainOrchestrator
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.mainController() {
    val orchestrator by inject<MainOrchestrator>()

    route("/v1/main") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            val lang = call.request.acceptLanguage() ?: "en"

            val screen = orchestrator.getMainData(
                userId = userId,
                appVersion = appVersion,
                lang = lang
            )
            call.respond(screen)
        }
    }
}
