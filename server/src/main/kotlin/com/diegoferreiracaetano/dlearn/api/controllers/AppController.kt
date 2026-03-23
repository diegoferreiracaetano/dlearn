package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.orchestrator.app.Orchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.http.HttpHeaders.AcceptLanguage
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
            
            val header = AppHeader(
                paramUserAgent = call.request.header(UserAgent),
                paramLanguage = request.language ?: call.request.header(AcceptLanguage),
                paramCountry = request.country ?: call.request.header(AppConstants.X_COUNTRY),
                userId = "guest"
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
