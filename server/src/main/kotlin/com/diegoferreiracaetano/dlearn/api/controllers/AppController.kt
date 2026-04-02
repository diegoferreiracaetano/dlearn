package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.api.util.appHeader
import com.diegoferreiracaetano.dlearn.api.util.userId
import com.diegoferreiracaetano.dlearn.orchestrator.app.Orchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
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
            val header = call.appHeader

            orchestrator
                .execute(
                    request = request,
                    header = header,
                    userId = call.userId,
                ).collect { screen ->
                    call.respond(screen)
                }
        }
    }
}
