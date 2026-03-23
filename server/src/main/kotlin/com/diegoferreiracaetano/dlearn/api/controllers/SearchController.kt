package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.app.SearchOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.searchController(orchestrator: SearchOrchestrator) {
    route("/v1/search") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgent = call.request.header(UserAgent) ?: ""
            val query = call.request.queryParameters["q"]

            val request = AppRequest(
                path = "/search",
                params = query?.let { mapOf("q" to it) }
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
