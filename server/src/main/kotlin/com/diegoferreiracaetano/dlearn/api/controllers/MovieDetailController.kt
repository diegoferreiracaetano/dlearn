package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.orchestrator.app.MovieDetailOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.movieDetailController(orchestrator: MovieDetailOrchestrator) {
    route("/v1/movie") {
        get("{movieId}") {
            val movieId = call.parameters["movieId"] ?: return@get call.respond("Missing movieId")
            val userId = call.request.queryParameters["userId"] ?: "guest"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            val lang = call.request.acceptLanguage() ?: "en"

            val request = AppRequest(
                path = "/movie/$movieId",
                params = mapOf(NavigationRoutes.MOVIE_ID_ARG to movieId)
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
