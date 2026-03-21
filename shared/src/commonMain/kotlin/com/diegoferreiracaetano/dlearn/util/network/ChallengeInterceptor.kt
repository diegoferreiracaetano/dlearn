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
            // Usamos o interceptor no pipeline de recebimento
            scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                if (response.status.value == 428) {
                    val responseBody = response.body<String>()
                    
                    val session = try {
                        plugin.json.decodeFromString<ChallengeSession>(responseBody)
                    } catch (e: Exception) {
                        proceedWith(response)
                        return@intercept
                    }

                    // Suspende a requisição original e aguarda a resolução do desafio (UI)
                    val result = plugin.engine.resolve(session)

                    if (result is ChallengeResult.Success) {
                        val originalRequest = response.call.request
                        
                        // Executa a requisição novamente com os tokens de validação
                        val retryResponse = scope.request {
                            takeFrom(originalRequest)
                            originalRequest.content.let { setBody(it) }
                            
                            // Adiciona os headers resultantes do desafio (ex: token validado)
                            result.data.forEach { (key, value) ->
                                header(key, value)
                            }
                            // Garante o envio do transactionId se necessário
                            header(SecurityConstants.HEADER_CHALLENGE_TOKEN, session.transactionId)
                        }
                        
                        // Substitui a resposta original pela resposta da nova tentativa
                        proceedWith(retryResponse)
                    } else {
                        proceedWith(response)
                    }
                } else {
                    proceedWith(response)
                }
            }
        }
    }
}
