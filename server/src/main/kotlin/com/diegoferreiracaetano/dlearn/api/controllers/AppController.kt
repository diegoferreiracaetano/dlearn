package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants.X_COUNTRY
import com.diegoferreiracaetano.dlearn.AppConstants.X_NOTIFICATIONS_ENABLED
import com.diegoferreiracaetano.dlearn.api.util.userId
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
