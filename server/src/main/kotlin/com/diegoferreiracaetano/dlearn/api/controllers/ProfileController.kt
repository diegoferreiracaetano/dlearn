package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.orchestrator.ProfileOrchestrator
import io.ktor.server.application.call
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.profileController() {
    val orchestrator by inject<ProfileOrchestrator>()

    route("/v1/profile") {
        get {
            val userId = call.parameters["userId"] ?: "default_user"
            val appVersion = call.request.headers["X-App-Version"]?.toIntOrNull() ?: 1
            val lang = call.request.acceptLanguage() ?: "en"
            
            val screen = orchestrator.getProfileData(userId, appVersion, lang)
            call.respond(screen)
        }

        get("/edit") {
            val userId = call.parameters["userId"] ?: "default_user"
            val lang = call.request.acceptLanguage() ?: "en"

            val screen = orchestrator.getEditProfileData(userId, lang)
            call.respond(screen)
        }

        post("/update") {
            val userId = call.parameters["userId"] ?: "default_user"
            val data = call.receive<Map<String, String>>()
            orchestrator.updateProfile(userId, data)
            call.respond(mapOf("status" to "success"))
        }
    }
}
