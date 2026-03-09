package com.diegoferreiracaetano.dlearn.data.movie.remote

import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieDetailRepositoryRemote(private val httpClient: HttpClient) : MovieDetailRepository {
    override fun getMovieDetail(movieId: String): Flow<Screen> = flow {
        val response = httpClient.get("v1/movie/$movieId").body<Screen>()
        emit(response)
    }
}
