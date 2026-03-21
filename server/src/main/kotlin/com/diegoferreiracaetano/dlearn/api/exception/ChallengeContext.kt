package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeCode
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeError
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey
import org.koin.ktor.ext.get

object ChallengeAttributes {
    val Preference = AttributeKey<ChallengeType>("ChallengePreference")
    val Token = AttributeKey<String>("ChallengeToken")
}

class ChallengeException(val error: ChallengeError) : RuntimeException(error.message)

/**
 * Configuração para o Plugin de Desafio
 */
class ChallengeConfig {
    var type: ChallengeType = ChallengeType.UNKNOWN
}

/**
 * Plugin moderno e escopado para gerenciar Desafios (MFA)
 */
val ChallengePlugin = createRouteScopedPlugin("ChallengePlugin", ::ChallengeConfig) {
    val passwordDataService = application.get<PasswordDataService>()
    val i18nProvider = application.get<I18nProvider>()

    onCall { call ->
        val type = pluginConfig.type
        call.attributes.put(ChallengeAttributes.Preference, type)
        
        val token = call.request.header(SecurityConstants.HEADER_CHALLENGE_TOKEN)
        val lang = call.request.acceptLanguage() ?: "en"

        if (token == null || !passwordDataService.validateChallenge(token)) {
            val userId = call.request.queryParameters["userId"] ?: "system_user"
            val transactionId = passwordDataService.generateChallenge(userId) 
            
            throw ChallengeException(
                ChallengeError(
                    code = ChallengeCode.CHALLENGE_REQUIRED,
                    message = i18nProvider.getString(AppStringType.PASSWORD_OTP_REQUIRED, lang),
                    challengeToken = transactionId
                )
            )
        }

        call.attributes.put(ChallengeAttributes.Token, token)
    }
}

/**
 * DSL moderna que instala o Plugin escopado.
 */
fun Route.challengePreference(
    type: ChallengeType, 
    build: Route.(token: ApplicationCall.() -> String?) -> Unit
): Route {
    val routeWithPlugin = createChild(object : RouteSelector() {
        override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
            RouteSelectorEvaluation.Constant
    })
    
    // Instala o plugin moderno na rota
    routeWithPlugin.install(ChallengePlugin) {
        this.type = type
    }
    
    routeWithPlugin.build { challengeTokenProvider }
    return routeWithPlugin
}

private val ApplicationCall.challengeTokenProvider: String? 
    get() = attributes.getOrNull(ChallengeAttributes.Token)

val ApplicationCall.challengeToken: String? 
    get() = challengeTokenProvider
