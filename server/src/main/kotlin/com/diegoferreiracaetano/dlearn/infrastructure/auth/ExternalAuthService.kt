package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider

interface ExternalAuthService {
    val provider: AccountProvider

    fun canHandle(metadata: Map<String, String>): Boolean

    suspend fun authenticate(metadata: Map<String, String>): Map<String, String>
}
