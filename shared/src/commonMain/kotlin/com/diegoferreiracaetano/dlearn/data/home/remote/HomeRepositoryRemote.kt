package com.diegoferreiracaetano.dlearn.data.home.remote

import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryRemote(private val httpClient: HttpClient) : HomeRepository {
    override fun getHome(type: HomeFilterType, search: String?): Flow<Home> = flow {
        val response = httpClient.get("home") {
            parameter("type", type.name)
            search?.let { parameter("search", it) }
        }.body<Home>()
        emit(response)
    }
}
