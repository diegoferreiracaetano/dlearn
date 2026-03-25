package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.util.appHeader
import com.diegoferreiracaetano.dlearn.orchestrator.app.SearchOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.searchController() {
    val orchestrator by inject<SearchOrchestrator>()

    route("/v1/search") {
        get("/main") {
            val header = call.appHeader

            orchestrator.searchMain(header).collect { screen ->
                call.respond(screen)
            }
        }

        get("/result") {
            val query = call.request.queryParameters["q"]
            val header = call.appHeader

            orchestrator.searchContent(header, query.orEmpty()).collect { screen ->
                call.respond(screen)
            }
        }
    }
}
