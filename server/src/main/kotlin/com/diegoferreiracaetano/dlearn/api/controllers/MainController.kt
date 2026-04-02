package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.util.appHeader
import com.diegoferreiracaetano.dlearn.api.util.userId
import com.diegoferreiracaetano.dlearn.orchestrator.app.MainOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.mainController() {
    val orchestrator by inject<MainOrchestrator>()

    route("/v1/main") {
        get {
            val request = AppRequest(path = "/main")

            orchestrator
                .execute(
                    request = request,
                    header = call.appHeader,
                    userId = call.userId,
                ).collect { screen ->
                    call.respond(screen)
                }
        }
    }
}
