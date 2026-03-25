package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants.X_COUNTRY
import com.diegoferreiracaetano.dlearn.AppConstants.X_NOTIFICATIONS_ENABLED
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.api.util.userId
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.orchestrator.app.MovieDetailOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.AcceptLanguage
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.movieDetailController() {

    val orchestrator by inject<MovieDetailOrchestrator>()

    route("/v1/movie") {
        get("{movieId}") {
            val movieId = call.parameters["movieId"] ?: return@get call.respond("Missing movieId")
            val request = AppRequest(
                path = "/movie/$movieId",
                params = mapOf(NavigationRoutes.MOVIE_ID_ARG to movieId)
            )

            val header = AppHeader(
                paramUserAgent = call.request.header(UserAgent),
                paramLanguage = call.request.header(AcceptLanguage),
                paramCountry = call.request.header(X_COUNTRY),
                notificationsEnabled = call.request.header(X_NOTIFICATIONS_ENABLED)?.toBoolean() ?: true,
                userId = call.userId
            )

            orchestrator.execute(
                request = request,
                header = header
            ).collect { screen ->
                call.respond(screen)
            }
        }
    }
}
