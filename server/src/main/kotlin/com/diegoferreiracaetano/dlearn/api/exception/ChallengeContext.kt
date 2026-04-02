package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.api.util.userId
import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeCode
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeError
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.header
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext
import io.ktor.util.AttributeKey
import org.koin.ktor.ext.get

// Nomes das chaves para busca segura
const val CHALLENGE_PREFERENCE_KEY = "ChallengePreference"
const val CHALLENGE_TOKEN_KEY = "ChallengeToken"

// Constantes para uso interno do Plugin
internal val ChallengePreferenceAttrKey = AttributeKey<ChallengeType>(CHALLENGE_PREFERENCE_KEY)
internal val ChallengeTokenAttrKey = AttributeKey<String>(CHALLENGE_TOKEN_KEY)

class ChallengeException(
    val error: ChallengeError,
) : RuntimeException(error.message)

class ChallengeConfig {
    var type: ChallengeType = ChallengeType.UNKNOWN
}

/**
 * Plugin de Desafio que agora utiliza o ChallengeDataService especializado.
 */
val ChallengePlugin =
    createRouteScopedPlugin("ChallengePlugin", ::ChallengeConfig) {
        val challengeDataService = application.get<ChallengeDataService>()
        val i18nProvider = application.get<I18nProvider>()

        onCall { call ->
            val type = pluginConfig.type
            call.attributes.put(ChallengePreferenceAttrKey, type)

            val token = call.request.header(SecurityConstants.HEADER_CHALLENGE_TOKEN)
            val lang = call.request.acceptLanguage() ?: "en"

            if (token == null || !challengeDataService.isTokenValidated(token)) {
                val userId =
                    try {
                        call.userId
                    } catch (e: Exception) {
                        "anonymous"
                    }
                val transactionId = challengeDataService.generateChallenge(userId)

                throw ChallengeException(
                    ChallengeError(
                        code = ChallengeCode.CHALLENGE_REQUIRED,
                        message = i18nProvider.getString(AppStringType.PASSWORD_OTP_REQUIRED, lang),
                        challengeToken = transactionId,
                    ),
                )
            }

            call.attributes.put(ChallengeTokenAttrKey, token)
        }
    }

fun Route.challengePreference(
    type: ChallengeType,
    build: Route.(token: ApplicationCall.() -> String?) -> Unit,
): Route {
    val routeWithPlugin =
        createChild(
            object : RouteSelector() {
                override suspend fun evaluate(
                    context: RoutingResolveContext,
                    segmentIndex: Int,
                ): RouteSelectorEvaluation = RouteSelectorEvaluation.Constant
            },
        )

    routeWithPlugin.install(ChallengePlugin) {
        this.type = type
    }

    routeWithPlugin.build { challengeTokenProvider }
    return routeWithPlugin
}

private val ApplicationCall.challengeTokenProvider: String?
    get() = attributes.getOrNull(ChallengeTokenAttrKey)

val ApplicationCall.challengeToken: String?
    get() = challengeTokenProvider
