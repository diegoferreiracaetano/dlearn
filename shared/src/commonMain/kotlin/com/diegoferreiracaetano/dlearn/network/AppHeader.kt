package com.diegoferreiracaetano.dlearn.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AppHeader(
    val paramUserAgent: String? = null,
    val paramLanguage: String? = null,
    val paramCountry: String? = null,
    val notificationsEnabled: Boolean = true,
) {
    @Transient
    val userAgent: AppUserAgent = AppUserAgent.fromHeader(paramUserAgent)

    @Transient
    val language: String = paramLanguage?.split(",")?.firstOrNull()?.trim() ?: "en-US"

    @Transient
    val country: String? = paramCountry ?: language.split("-").getOrNull(1)
}
