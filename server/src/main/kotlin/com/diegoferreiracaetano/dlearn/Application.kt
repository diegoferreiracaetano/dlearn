package com.diegoferreiracaetano.dlearn

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.diegoferreiracaetano.dlearn.api.controllers.*
import com.diegoferreiracaetano.dlearn.api.exception.configureStatusPages
import com.diegoferreiracaetano.dlearn.di.serverModule
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    install(Koin) {
        modules(serverModule)
    }

    val tokenService by inject<TokenService>()

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "dlearn"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("dlearn-secret-key-change-it-in-prod"))
                    .withAudience("dlearn-audience")
                    .withIssuer("com.diegoferreiracaetano.dlearn")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    install(CachingHeaders) {
        options { _, outgoingContent ->
            // Não aplica cache para erros (status >= 400)
            if (outgoingContent.status != null && outgoingContent.status!!.value >= 400) return@options null
            
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Application.Json -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 300))
                else -> null
            }
        }
    }

    // StatusPages deve vir após a maioria dos plugins que alteram a resposta
    configureStatusPages()

    routing {
        swaggerUI(path = "swagger", swaggerFile = "documentation.yaml")
        openAPI(path = "openapi", swaggerFile = "documentation.yaml")

        authController()
        challengeController()
        passwordController() // Movi para fora do authenticate para suportar fluxo deslogado

        authenticate("auth-jwt") {
            mainController()
            appController()
            userController()
        }
    }
}
