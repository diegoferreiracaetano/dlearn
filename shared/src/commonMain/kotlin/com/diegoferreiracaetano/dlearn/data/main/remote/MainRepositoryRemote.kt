package com.diegoferreiracaetano.dlearn.data.main.remote

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepositoryRemote(private val httpClient: HttpClient) : MainRepository {
    override fun getMain(
        type: HomeFilterType,
        search: String?
    ): Flow<Screen> = flow {
        val response = httpClient.get("v1/main") {
            parameter("type", type.name)
            search?.let { parameter("search", it) }
        }.body<Screen>()
        emit(response)
    }

    override fun getContent(
        type: HomeFilterType,
        search: String?
    ): Flow<Screen> = flow {
        val response = httpClient.get("v1/main/content") {
            parameter("type", type.name)
            search?.let { parameter("search", it) }
        }.body<Screen>()
        emit(response)
    }
}
