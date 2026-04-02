package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbWatchProvidersResponse(
    val results: Map<String, WatchProvidersCountry> = emptyMap(),
)

@Serializable
data class WatchProvidersCountry(
    val link: String? = null,
    val flatrate: List<WatchProviderRemote>? = null,
    val rent: List<WatchProviderRemote>? = null,
    val buy: List<WatchProviderRemote>? = null,
)

@Serializable
data class WatchProviderRemote(
    @SerialName("display_priority") val displayPriority: Int? = null,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("provider_id") val providerId: Int? = null,
    @SerialName("provider_name") val providerName: String? = null,
)
