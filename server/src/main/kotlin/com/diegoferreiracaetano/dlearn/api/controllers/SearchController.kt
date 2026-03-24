package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants.X_COUNTRY
import com.diegoferreiracaetano.dlearn.api.util.userId
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.orchestrator.app.SearchOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.AcceptLanguage
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.searchController() {

    val orchestrator by inject<SearchOrchestrator>()

    route("/v1/search") {
        get {

            val query = call.request.queryParameters["q"]

            val request = AppRequest(
                path = "/search",
                params = query?.let { mapOf("q" to it) }
            )

            val header = AppHeader(
                paramUserAgent = call.request.header(UserAgent),
                paramLanguage = request.language ?: call.request.header(AcceptLanguage),
                paramCountry = request.country ?: call.request.header(X_COUNTRY),
                notificationsEnabled = request.notificationsEnabled ?: true,
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
