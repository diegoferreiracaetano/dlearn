package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.util.PreconditionRequired
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import org.koin.ktor.ext.inject

fun Application.configureStatusPages() {
    val challengeMapper by inject<ChallengeMapper>()

    install(StatusPages) {
        // Trata Desafios (428)
        exception<Throwable> { call, cause ->
            val challenge = challengeMapper.toChallengeSession(cause)
            if (challenge != null) {
                call.respond(HttpStatusCode.PreconditionRequired, challenge)
                return@exception
            }
            
            // Tratamento genérico para erros de negócio não mapeados como desafio
            call.respondText(
                text = "Error: ${cause.localizedMessage ?: "Unknown error"}",
                status = if (cause is IllegalArgumentException) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError
            )
        }
    }
}
