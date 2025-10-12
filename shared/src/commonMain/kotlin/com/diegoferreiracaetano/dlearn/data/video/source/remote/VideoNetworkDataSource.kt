package com.diegoferreiracaetano.dlearn.data.video.source.remote

import com.diegoferreiracaetano.dlearn.util.getLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText

class VideoNetworkDataSource(
    private val httpClient: HttpClient,
    private val apiKey: String,
) {
//    suspend fun fetchMovies(): List<VideoRemote> {
//        val response = httpClient.get("3/movie/popular") {
//            parameter("api_key", apiKey)
//        }
//
//        getLogger().d("TESTE", "Response: ${response.bodyAsText()}")
//
//        return response.body<VideoResponse>().results
//    }
}
