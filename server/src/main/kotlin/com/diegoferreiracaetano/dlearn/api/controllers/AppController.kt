package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.appController() {
    route("/v1/app") {
        post {
            val request = call.receive<AppRequest>()
            
            // O backend decide qual lógica executar baseado no request.path
            // Pode chamar diferentes orchestrators, repositórios ou serviços.
            // Exemplo:
            // val screen = appOrchestrator.execute(request.path, request.params, request.metadata)
            // call.respond(screen)
        }
    }
}
