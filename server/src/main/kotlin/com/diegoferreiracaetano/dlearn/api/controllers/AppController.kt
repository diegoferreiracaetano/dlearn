package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.orchestrator.app.Orchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.appController() {
    val orchestrator by inject<Orchestrator>()

    route("/v1/app") {
        post {
            val request = call.receive<AppRequest>()
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgentHeader = call.request.header(UserAgent) ?: ""
            val userAgent = AppUserAgent.fromHeader(userAgentHeader)

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
