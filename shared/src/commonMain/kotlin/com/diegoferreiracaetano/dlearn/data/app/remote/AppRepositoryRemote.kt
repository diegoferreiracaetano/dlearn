package com.diegoferreiracaetano.dlearn.data.app.remote

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.network.AppUserAgentProvider
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepositoryRemote(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val userAgentProvider: AppUserAgentProvider,
    private val preferencesRepository: PreferencesRepository,
    private val sessionManager: SessionManager
) : AppRepository {

    override fun execute(
        path: String,
        params: Map<String, String>?,
        metadata: Map<String, String>?
    ): Flow<Screen> = flow {
        // Special case for login screen structure
        if (path == "/auth/login" && params == null) {
            val response = httpClient.get("$baseUrl/auth/login")
            emit(response.body<Screen>())
            return@flow
        }

        // Special case for login submission
        if (path == "/auth/login" && params != null) {
            val response = httpClient.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(params)
            }
            val authResponse = response.body<AuthResponse>()
            
            // Persist session if login was successful
            if (authResponse.accessToken != null && authResponse.refreshToken != null && authResponse.user != null) {
                sessionManager.login(
                    user = authResponse.user,
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken
                )
            }

            authResponse.screen?.let { emit(it) }
            return@flow
        }

        val request = AppRequest(
            path = path,
            params = params,
            metadata = metadata,
            language = preferencesRepository.language,
            country = preferencesRepository.country,
            notificationsEnabled = preferencesRepository.notificationsEnabled
        )

        val response = httpClient.post("$baseUrl/v1/app") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val screen = response.body<Screen>()
        emit(screen)
    }
}
