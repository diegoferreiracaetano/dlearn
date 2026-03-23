package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.orchestrator.app.ProfileOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.profileController() {
    val orchestrator by inject<ProfileOrchestrator>()

    route("/v1/profile") {
        get {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgentHeader = call.request.header(UserAgent) ?: ""
            val userAgent = AppUserAgent.fromHeader(userAgentHeader)


            val request = AppRequest(path = "/profile")

            orchestrator.execute(
                request = request,
                userId = userId,
                userAgent = userAgent
            ).collect { screen ->
                call.respond(screen)
            }
        }

        get("/edit") {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgentHeader = call.request.header(UserAgent) ?: ""
            val userAgent = AppUserAgent.fromHeader(userAgentHeader)


            val request = AppRequest(path = "/profile/edit")

            orchestrator.execute(
                request = request,
                userId = userId,
                userAgent = userAgent
            ).collect { screen ->
                call.respond(screen)
            }
        }

        post("/update") {
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgentHeader = call.request.header(UserAgent) ?: ""
            val userAgent = AppUserAgent.fromHeader(userAgentHeader)

            val data = call.receive<Map<String, String>>()

            val request = AppRequest(
                path = "/profile/update",
                params = data
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
