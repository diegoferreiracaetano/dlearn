package com.diegoferreiracaetano.dlearn.data.search.remote

import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryRemote(private val httpClient: HttpClient) : SearchRepository {
    override fun getSearchMain(): Flow<Screen> = flow {
        val response = httpClient.get("v1/search").body<Screen>()
        emit(response)
    }

    override fun search(query: String): Flow<Screen> = flow {
        val response = httpClient.get("v1/search/content") {
            parameter("q", query)
        }.body<Screen>()
        emit(response)
    }
}
