package com.diegoferreiracaetano.dlearn.data.home.remote

import com.diegoferreiracaetano.dlearn.ApiEndpoints
import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryRemote(
    private val httpClient: HttpClient,
) : HomeRepository {
    override fun getHome(type: HomeFilterType): Flow<Screen> =
        flow {
            val response =
                httpClient
                    .get(ApiEndpoints.V1_HOME) {
                        parameter(TmdbConstants.PARAM_TYPE, type.name)
                    }.body<Screen>()
            emit(response)
        }
}
