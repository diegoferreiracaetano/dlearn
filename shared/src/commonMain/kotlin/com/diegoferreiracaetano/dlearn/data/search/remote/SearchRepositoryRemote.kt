package com.diegoferreiracaetano.dlearn.data.search.remote

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
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

class SearchRepositoryRemote(private val httpClient: HttpClient) : SearchRepository {
    override fun getSearchMain(): Flow<Screen> = flow {
        val response = httpClient.post("v1/app") {
            contentType(ContentType.Application.Json)
            setBody(AppRequest(path = AppNavigationRoute.SEARCH))
        }.body<Screen>()
        emit(response)
    }

    override fun search(query: String): Flow<Screen> = flow {
        val response = httpClient.post("v1/app") {
            contentType(ContentType.Application.Json)
            setBody(
                AppRequest(
                    path = AppNavigationRoute.SEARCH,
                    params = mapOf(AppQueryParam.Q to query)
                )
            )
        }.body<Screen>()
        emit(response)
    }
}
