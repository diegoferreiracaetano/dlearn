package com.diegoferreiracaetano.dlearn.data.movie.remote

import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
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

class MovieDetailRepositoryRemote(
    private val httpClient: HttpClient,
) : MovieDetailRepository {
    override fun execute(request: AppRequest): Flow<Screen> =
        flow {
            val response =
                httpClient
                    .post("/v1/app") {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }.body<Screen>()
            emit(response)
        }
}
