package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.orchestrator.SearchOrchestrator
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.searchController(orchestrator: SearchOrchestrator) {
    route("/v1/search") {
        get {
            val userId = call.request.queryParameters["userId"] ?: AppConstants.GUEST_USER_ID
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: AppConstants.DEFAULT_APP_VERSION
            val lang = call.request.acceptLanguage() ?: AppConstants.DEFAULT_LANG
            val query = call.request.queryParameters["q"] ?: ""
            
            val screen = orchestrator.search(
                userId = userId,
                appVersion = appVersion,
                lang = lang,
                query = query
            )
            
            call.respond(screen)
        }
    }
}
