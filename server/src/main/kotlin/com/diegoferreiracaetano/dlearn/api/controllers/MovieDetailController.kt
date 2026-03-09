package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.MovieDetailOrchestrator
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.movieDetailController(orchestrator: MovieDetailOrchestrator) {
    route("/v1/movie") {
        get("/{movieId}") {
            val movieId = call.parameters["movieId"] ?: return@get
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            val lang = call.request.acceptLanguage() ?: "en"
            
            val screen = orchestrator.getMovieDetail(movieId, appVersion, lang)
            call.respond(screen)
        }
    }
}
