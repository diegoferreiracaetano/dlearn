package com.diegoferreiracaetano.dlearn

import com.diegoferreiracaetano.dlearn.api.controllers.*
import com.diegoferreiracaetano.dlearn.api.exception.configureStatusPages
import com.diegoferreiracaetano.dlearn.di.serverModule
import com.diegoferreiracaetano.dlearn.orchestrator.app.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.app.MovieDetailOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.app.SearchOrchestrator
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
    install(Koin) {
        modules(serverModule)
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    configureStatusPages()

    install(CachingHeaders) {
        options { _, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Application.Json -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 300))
                else -> null
            }
        }
    }

    val homeOrchestrator by inject<HomeOrchestrator>()
    val movieDetailOrchestrator by inject<MovieDetailOrchestrator>()
    val searchOrchestrator by inject<SearchOrchestrator>()

    routing {
        swaggerUI(path = "swagger", swaggerFile = "documentation.yaml")
        openAPI(path = "openapi", swaggerFile = "documentation.yaml")

        mainController()
        homeController(homeOrchestrator)
        profileController()
        movieDetailController(movieDetailOrchestrator)
        searchController(searchOrchestrator)
        appController()
        passwordController()
        challengeController() // Registrado!
    }
}
