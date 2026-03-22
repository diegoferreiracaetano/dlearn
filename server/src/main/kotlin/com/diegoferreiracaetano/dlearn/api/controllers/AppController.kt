package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.AppOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.appController() {
    val appOrchestrator by inject<AppOrchestrator>()

    route("/v1/app") {
        post {
            val request = call.receive<AppRequest>()
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val lang = call.request.acceptLanguage() ?: "en"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1

            appOrchestrator.execute(
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
