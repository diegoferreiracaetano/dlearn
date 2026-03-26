package com.diegoferreiracaetano.dlearn.data.password.remote

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.auth.network.AuthMode
import com.diegoferreiracaetano.dlearn.auth.network.auth
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordRepositoryRemote(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) : PasswordRepository {
    
    override fun changePassword(newPassword: String): Flow<ChangePasswordResponse> = flow {
        val userId = try {
            if (sessionManager.isLoggedIn.value) {
                sessionManager.user().id
            } else {
                AppConstants.GUEST_USER_ID
            }
        } catch (e: Exception) {
            AppConstants.GUEST_USER_ID
        }

        val request = ChangePasswordRequest(userId = userId, newPassword = newPassword)
        
        val response = httpClient.post("v1/password/change") {
            auth(AuthMode.OPTIONAL)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ChangePasswordResponse>()
        
        emit(response)
    }
}
