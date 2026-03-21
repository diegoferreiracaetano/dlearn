package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

/**
 * Interceptor de rede inteligente para Desafios de Segurança (MFA).
 */
class ChallengeInterceptor(
    private val engine: ChallengeEngine,
    private val json: Json
) {
    class Config {
        lateinit var engine: ChallengeEngine
        lateinit var json: Json
    }

    companion object Plugin : HttpClientPlugin<Config, ChallengeInterceptor> {
        override val key: AttributeKey<ChallengeInterceptor> = AttributeKey("ChallengeInterceptor")

        override fun prepare(block: Config.() -> Unit): ChallengeInterceptor {
            val config = Config().apply(block)
            return ChallengeInterceptor(config.engine, config.json)
        }

        override fun install(plugin: ChallengeInterceptor, scope: HttpClient) {
            scope.receivePipeline.intercept(HttpReceivePipeline.After) { 
                val response = subject as? HttpResponse ?: return@intercept
                
                if (response.status.value == 428) {
                    val originalRequest = response.call.request
                    val responseBody = response.body<String>()
                    
                    val session = try {
                        plugin.json.decodeFromString<ChallengeSession>(responseBody)
                    } catch (e: Exception) {
                        return@intercept
                    }

                    val preferenceHeader = originalRequest.headers[SecurityConstants.HEADER_CHALLENGE_PREFERENCE]
                    val preferredType = ChallengeType.entries.find { it.name == preferenceHeader }

                    val filteredSession = if (preferredType != null) {
                        val preferredChallenge = session.challenges.find { it.challengeType == preferredType }
                        if (preferredChallenge != null) {
                            session.copy(challenges = listOf(preferredChallenge))
                        } else session
                    } else session

                    val result = plugin.engine.resolve(filteredSession)

                    if (result is ChallengeResult.Success) {
                        val retryCall = scope.request {
                            takeFrom(originalRequest)
                            
                            // Preserva o corpo da requisição original para o retry
                            originalRequest.content.let { setBody(it) }
                            
                            // Mantém o content type original
                            contentType(ContentType.Application.Json)
                            
                            // Remove headers de controle do interceptor para não sujar o retry
                            headers.remove(SecurityConstants.HEADER_CHALLENGE_TOKEN)
                            headers.remove(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)
                            
                            // Injeta os novos headers de sucesso (ex: X-Challenge-Token)
                            result.data.forEach { (key, value) ->
                                header(key, value)
                            }
                        }
                        
                        proceedWith(retryCall)
                    } else {
                        // Se o desafio falhou ou foi cancelado, permitimos que o erro 428 original 
                        // prossiga, mas o interceptor já cumpriu seu papel de tentar resolver.
                        proceedWith(response)
                    }
                }
            }
        }
    }
}
