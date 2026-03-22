package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.app.MainOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
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

            val request = AppRequest(path = "/main")

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
