package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode

interface SocialAuthManager {
    suspend fun googleSignIn(): SocialAuthResult
    suspend fun appleSignIn(): SocialAuthResult
    suspend fun facebookSignIn(): SocialAuthResult
}

sealed class SocialAuthResult {
    data class Success(val idToken: String, val accessToken: String? = null) : SocialAuthResult()
    data class Error(val error: AppErrorCode) : SocialAuthResult()
    object Cancelled : SocialAuthResult()
}
