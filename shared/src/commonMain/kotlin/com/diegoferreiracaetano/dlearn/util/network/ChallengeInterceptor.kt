package com.diegoferreiracaetano.dlearn.util.network

import com.diegoferreiracaetano.dlearn.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeSession
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

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
                    val responseBody = response.body<String>()
                    
                    val session = try {
                        plugin.json.decodeFromString<ChallengeSession>(responseBody)
                    } catch (e: Exception) {
                        return@intercept
                    }

                    // Aguarda a resolução do desafio pela UI
                    val result = plugin.engine.resolve(session)

                    if (result is ChallengeResult.Success) {
                        val originalRequest = response.call.request
                        
                        // Faz o RETRY da requisição original com os novos headers de validação
                        val retryCall = scope.request {
                            takeFrom(originalRequest)
                            originalRequest.content.let { setBody(it) }
                            
                            // Remove headers antigos de desafio para evitar duplicidade
                            headers.remove(SecurityConstants.HEADER_CHALLENGE_TOKEN)
                            
                            // Adiciona os novos headers (que contêm o token validado)
                            result.data.forEach { (key, value) ->
                                header(key, value)
                            }
                        }
                        
                        // Substitui o subject pela resposta do retry
                        proceedWith(retryCall)
                    }
                }
            }
        }
    }
}
