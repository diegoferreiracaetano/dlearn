package com.diegoferreiracaetano.dlearn.api

import com.diegoferreiracaetano.dlearn.api.controllers.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        // --- Documentation ---
        swaggerUI(path = "swagger", swaggerFile = "documentation.yaml")
        openAPI(path = "openapi", swaggerFile = "documentation.yaml")

        // --- Public Endpoints ---
        authController()
        challengeController()
        passwordController()

        // --- Authenticated Endpoints ---
        authenticate("auth-jwt") {
            mainController()
            appController()
            userController()
        }
    }
}
