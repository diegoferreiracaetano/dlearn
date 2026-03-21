package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.util.PreconditionRequired
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.util.AttributeKey
import org.koin.ktor.ext.inject

fun Application.configureStatusPages() {
    val challengeMapper by inject<ChallengeMapper>()

    install(StatusPages) {
        // Trata Desafios (428)
        exception<Throwable> { call, cause ->
            
            // Busca a preferência pelo nome da chave de forma ultra-segura
            val preferenceFromController = call.attributes.allKeys
                .find { it.name == CHALLENGE_PREFERENCE_KEY }
                ?.let { call.attributes[it as AttributeKey<ChallengeType>] }

            val preferenceHeader = call.request.header(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)
            
            val preferredType = preferenceFromController 
                ?: ChallengeType.entries.find { it.name == preferenceHeader }

            val challenge = challengeMapper.toChallengeSession(cause, preferredType)

            if (challenge != null) {
                call.respond(HttpStatusCode.PreconditionRequired, challenge)
                return@exception
            }
            
            call.respondText(
                text = "Error: ${cause.localizedMessage ?: "Unknown error"}",
                status = if (cause is IllegalArgumentException) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError
            )
        }
    }
}
