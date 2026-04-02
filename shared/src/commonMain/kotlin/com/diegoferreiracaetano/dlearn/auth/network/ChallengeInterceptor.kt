package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCancelledException
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import io.ktor.client.HttpClient
import io.ktor.client.call.save
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

/**
 * Interceptor de rede inteligente para Desafios de Segurança (MFA).
 */
class ChallengeInterceptor(
    private val engine: ChallengeEngine,
    private val json: Json,
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

        override fun install(
            plugin: ChallengeInterceptor,
            scope: HttpClient,
        ) {
            scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
                if (response.status.value == 428) {
                    val savedResponse = response.call.save().response

                    val responseBody =
                        try {
                            savedResponse.bodyAsText()
                        } catch (e: Exception) {
                            proceedWith(savedResponse)
                            return@intercept
                        }

                    val session =
                        try {
                            plugin.json.decodeFromString<ChallengeSession>(responseBody)
                        } catch (e: Exception) {
                            proceedWith(savedResponse)
                            return@intercept
                        }

                    println("DEBUG ChallengeInterceptor: resolve")

                    val result = plugin.engine.resolve(session)

                    if (result is ChallengeResult.Success) {
                        val validatedToken = result.data["validatedToken"] ?: ""

                        val originalRequest = savedResponse.call.request
                        val retryCall =
                            scope.request {
                                takeFrom(originalRequest)

                                val originalContent = originalRequest.content
                                if (originalContent !is OutgoingContent.NoContent) {
                                    setBody(originalContent)
                                }

                                contentType(ContentType.Application.Json)
                                header(SecurityConstants.HEADER_CHALLENGE_TOKEN, validatedToken)
                                header(SecurityConstants.HEADER_TRANSACTION_ID, session.transactionId)
                            }

                        proceedWith(retryCall)
                    } else {
                        throw ChallengeCancelledException()
                    }
                } else {
                    proceedWith(response)
                }
            }
        }
    }
}
