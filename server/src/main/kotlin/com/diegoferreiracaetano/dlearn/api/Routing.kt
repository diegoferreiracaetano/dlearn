package com.diegoferreiracaetano.dlearn.api

import com.diegoferreiracaetano.dlearn.api.controllers.appController
import com.diegoferreiracaetano.dlearn.api.controllers.authController
import com.diegoferreiracaetano.dlearn.api.controllers.challengeController
import com.diegoferreiracaetano.dlearn.api.controllers.mainController
import com.diegoferreiracaetano.dlearn.api.controllers.passwordController
import com.diegoferreiracaetano.dlearn.api.controllers.userController
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

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
