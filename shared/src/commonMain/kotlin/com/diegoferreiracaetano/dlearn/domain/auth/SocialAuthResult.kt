package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode

sealed class SocialAuthResult {
    data class Success(
        val idToken: String,
        val accessToken: String? = null,
    ) : SocialAuthResult()

    data class Failure(
        val error: AppErrorCode,
    ) : SocialAuthResult()

    data object Cancelled : SocialAuthResult()
}
