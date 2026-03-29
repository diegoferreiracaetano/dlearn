package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class ProviderSession(
    val provider: AccountProvider,
    val sessionId: String,
    val accountId: String? = null
)
