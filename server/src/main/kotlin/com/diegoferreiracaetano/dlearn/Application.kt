package com.diegoferreiracaetano.dlearn

import com.diegoferreiracaetano.dlearn.api.controllers.homeController
import com.diegoferreiracaetano.dlearn.api.controllers.mainController
import com.diegoferreiracaetano.dlearn.api.controllers.movieDetailController
import com.diegoferreiracaetano.dlearn.api.controllers.profileController
import com.diegoferreiracaetano.dlearn.di.serverModule
import com.diegoferreiracaetano.dlearn.orchestrator.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.MovieDetailOrchestrator
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.content.CachingOptions
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(CachingHeaders) {
        options { _, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Application.Json -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 300))
                else -> null
            }
        }
    }

    install(Koin) {
        modules(serverModule)
    }

    val homeOrchestrator by inject<HomeOrchestrator>()
    val movieDetailOrchestrator by inject<MovieDetailOrchestrator>()

    routing {
        // Swagger UI will look for the documentation.yaml in the resources/openapi folder
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")

        mainController()
        homeController(homeOrchestrator)
        profileController()
        movieDetailController(movieDetailOrchestrator)
    }
}
