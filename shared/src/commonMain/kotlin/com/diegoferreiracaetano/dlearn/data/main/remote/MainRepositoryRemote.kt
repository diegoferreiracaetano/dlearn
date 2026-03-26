package com.diegoferreiracaetano.dlearn.data.main.remote

import com.diegoferreiracaetano.dlearn.data.cache.toCache
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepositoryRemote(
    private val httpClient: HttpClient
) : MainRepository {
    override fun getMain(): Flow<Screen> = flow {
        val response = httpClient.get("v1/main").body<Screen>()
        emit(response)
    }.toCache(key = "main_screen_cache")
}
