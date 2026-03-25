package com.diegoferreiracaetano.dlearn.data.app.remote

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepositoryRemote(
    private val httpClient: HttpClient,
    private val preferencesRepository: PreferencesRepository
) : AppRepository {

    override fun execute(
        path: String,
        params: Map<String, String>?,
        metadata: Map<String, String>?
    ): Flow<Screen> = flow {
        val request = AppRequest(
            path = path,
            params = params,
            metadata = metadata
        )

        val response = httpClient.post("/v1/app") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val screen = response.body<Screen>()
        emit(screen)
    }
}
