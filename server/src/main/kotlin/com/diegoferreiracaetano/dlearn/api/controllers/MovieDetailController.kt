package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.orchestrator.app.MovieDetailOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.movieDetailController(orchestrator: MovieDetailOrchestrator) {
    route("/v1/movie") {
        get("{movieId}") {
            val movieId = call.parameters["movieId"] ?: return@get call.respond("Missing movieId")
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val userAgentHeader = call.request.header(UserAgent) ?: ""
            val userAgent = AppUserAgent.fromHeader(userAgentHeader)

            val request = AppRequest(
                path = "/movie/$movieId",
                params = mapOf(NavigationRoutes.MOVIE_ID_ARG to movieId)
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
