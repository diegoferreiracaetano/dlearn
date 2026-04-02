package com.diegoferreiracaetano.dlearn.data.profile.remote

import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryRemote(
    private val httpClient: HttpClient,
) : ProfileRepository {
    override fun getProfile(language: String?): Flow<Screen> =
        flow {
            val response =
                httpClient
                    .get("v1/profile") {
                        language?.let { header(HttpHeaders.AcceptLanguage, it) }
                    }.body<Screen>()
            emit(response)
        }
}
