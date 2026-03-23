package com.diegoferreiracaetano.dlearn.data.app.remote

import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepositoryRemote(
    private val httpClient: HttpClient
) : AppRepository {
    override fun execute(
        path: String,
        params: Map<String, String>?,
        metadata: Map<String, String>?
    ): Flow<Screen> = flow {
        println("DEBUG: AppRepositoryRemote - Executing POST $path")
        val response = httpClient.post(GATEWAY_PATH) {
            contentType(ContentType.Application.Json)
            setBody(AppRequest(path = path, params = params, metadata = metadata))
            metadata?.forEach { (key, value) ->
                header(key, value)
            }
        }.body<Screen>()
        
        // Log para ver se o servidor enviou o item como selecionado
        println("DEBUG: AppRepositoryRemote - Received Screen for $path")
        emit(response)
    }

    companion object {
        private const val GATEWAY_PATH = "v1/app"
    }
}
