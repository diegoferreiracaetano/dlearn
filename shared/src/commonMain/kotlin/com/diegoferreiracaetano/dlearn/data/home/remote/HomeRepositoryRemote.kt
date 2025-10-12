package com.diegoferreiracaetano.dlearn.data.home.remote

import com.diegoferreiracaetano.dlearn.data.home.mapper.toDomain
import com.diegoferreiracaetano.dlearn.data.home.model.HomeForClient
import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryRemote(private val httpClient: HttpClient) : HomeRepository {
    override fun getHome(): Flow<Home> = flow {
        val response = httpClient.get("home").body<HomeForClient>()
        emit(response.toDomain())
    }
}
