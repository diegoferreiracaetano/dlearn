package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.auth.RefreshTokenRequest
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val client: HttpClient,
) {
    private val mutex = Mutex()

    suspend fun intercept(request: HttpRequestBuilder) {
        val mode = request.attributes.getOrNull(AuthModeKey) ?: AuthMode.REQUIRED
        if (mode == AuthMode.NONE) return

        val token = sessionManager.token()
        if (token != null) {
            request.header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun handleUnauthorized(response: HttpResponse): Boolean {
        val mode = response.request.attributes.getOrNull(AuthModeKey) ?: AuthMode.REQUIRED
        if (mode == AuthMode.NONE) return false

        if (response.status == HttpStatusCode.Unauthorized) {
            return mutex.withLock {
                val currentToken = sessionManager.token()
                val requestToken =
                    response.request.headers[HttpHeaders.Authorization]?.removePrefix("Bearer ")

                if (currentToken != requestToken && currentToken != null) {
                    return@withLock true
                }

                val refreshToken = sessionManager.refreshToken() ?: return@withLock false

                return@withLock try {
                    val refreshResponse =
                        client.post("/v1/auth/refresh") {
                            auth(AuthMode.NONE)
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokenRequest(refreshToken))
                        }

                    if (refreshResponse.status == HttpStatusCode.OK) {
                        val auth = refreshResponse.body<AuthResponse>()
                        if (auth.accessToken != null && auth.refreshToken != null && auth.user != null) {
                            sessionManager.login(auth.user, auth.accessToken, auth.refreshToken)
                            true
                        } else {
                            sessionManager.logout()
                            false
                        }
                    } else {
                        sessionManager.logout()
                        false
                    }
                } catch (
                    @Suppress("SwallowedException") e: Exception,
                ) {
                    sessionManager.logout()
                    false
                }
            }
        }
        return false
    }
}
