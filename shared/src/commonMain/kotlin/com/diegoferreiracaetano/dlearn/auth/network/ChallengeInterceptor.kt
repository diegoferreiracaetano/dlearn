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
 * Detecta erros 428 (Precondition Required), dispara o fluxo de desafio
 * e repete a requisição original com o token validado.
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
                
                // 428 Precondition Required indica que um desafio de segurança é necessário
                if (response.status.value == 428) {
                    val originalRequest = response.call.request
                    val responseBody = response.body<String>()
                    
                    val session = try {
                        plugin.json.decodeFromString<ChallengeSession>(responseBody)
                    } catch (e: Exception) {
                        // Se não conseguirmos ler a sessão, deixamos o erro original prosseguir
                        return@intercept
                    }

                    // Filtra o tipo preferido se o header X-Challenge-Preference estiver presente
                    val preferenceHeader = originalRequest.headers[SecurityConstants.HEADER_CHALLENGE_PREFERENCE]
                    val preferredType = ChallengeType.entries.find { it.name == preferenceHeader }

                    val filteredSession = if (preferredType != null) {
                        val preferredChallenge = session.challenges.find { it.challengeType == preferredType }
                        if (preferredChallenge != null) {
                            session.copy(challenges = listOf(preferredChallenge))
                        } else session
                    } else session

                    // Dispara o fluxo de UI/Resolução através do ChallengeEngine
                    val result = plugin.engine.resolve(filteredSession)

                    if (result is ChallengeResult.Success) {
                        // O desafio foi resolvido! Pegamos o token resultante (validatedToken)
                        val validatedToken = result.data["validatedToken"] ?: ""
                        
                        val retryCall = scope.request {
                            takeFrom(originalRequest)
                            
                            // Preserva o corpo da requisição original (importante para POST/PUT)
                            originalRequest.content.let { setBody(it) }
                            
                            // Garante o Content-Type original
                            contentType(ContentType.Application.Json)
                            
                            // Injeta os headers de prova para que o backend aceite a requisição agora
                            header(SecurityConstants.HEADER_CHALLENGE_TOKEN, validatedToken)
                            header(SecurityConstants.HEADER_TRANSACTION_ID, session.transactionId)
                            
                            // Limpa headers de controle que não devem ser reenviados
                            headers.remove(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)
                        }
                        
                        // Substitui a resposta de erro 428 pelo resultado da nova tentativa
                        proceedWith(retryCall)
                    } else {
                        // Se o usuário cancelou ou o desafio falhou, mantemos o 428 original
                        proceedWith(response)
                    }
                }
            }
        }
    }
}
