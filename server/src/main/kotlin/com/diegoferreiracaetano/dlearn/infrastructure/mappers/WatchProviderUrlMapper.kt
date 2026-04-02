package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.util.WatchProviderIds
import com.diegoferreiracaetano.dlearn.util.WatchProviderPaths
import com.diegoferreiracaetano.dlearn.util.WatchProviderSchemes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class WatchProviderUrls(
    val appUrl: String?,
    val webUrl: String?,
    val tmdbUrl: String,
)

class WatchProviderUrlMapper {
    fun buildUrls(
        providerId: Int?,
        title: String,
        imdbId: String?,
        tmdbFallbackUrl: String?,
    ): WatchProviderUrls {
        val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())

        val providerUrl =
            when (providerId) {
                WatchProviderIds.NETFLIX, WatchProviderIds.NETFLIX_ALT -> {
                    val app = "${WatchProviderSchemes.NETFLIX}${WatchProviderPaths.NETFLIX_SEARCH}$encodedTitle"
                    val web = "${WatchProviderPaths.NETFLIX_WEB_SEARCH}$encodedTitle"
                    app to web
                }

                WatchProviderIds.DISNEY_PLUS -> {
                    val app = "${WatchProviderSchemes.DISNEY_PLUS}${WatchProviderPaths.DISNEY_SEARCH}$encodedTitle"
                    val web = "${WatchProviderPaths.DISNEY_WEB_SEARCH}$encodedTitle"
                    app to web
                }

                WatchProviderIds.AMAZON_PRIME -> {
                    val app =
                        if (imdbId != null) {
                            "${WatchProviderSchemes.AMAZON_PRIME}${WatchProviderPaths.AMAZON_DETAIL}$imdbId"
                        } else {
                            "${WatchProviderSchemes.AMAZON_PRIME}${WatchProviderPaths.AMAZON_SEARCH}$encodedTitle"
                        }
                    val web =
                        if (imdbId != null) {
                            "${WatchProviderPaths.AMAZON_WEB_DETAIL}$imdbId"
                        } else {
                            "${WatchProviderPaths.AMAZON_WEB_SEARCH}$encodedTitle"
                        }
                    app to web
                }

                WatchProviderIds.APPLE_TV -> {
                    val app = "${WatchProviderSchemes.APPLE_TV}${WatchProviderPaths.APPLE_SEARCH}$encodedTitle"
                    val web = "${WatchProviderPaths.APPLE_WEB_SEARCH}$encodedTitle"
                    app to web
                }

                WatchProviderIds.HBO_MAX, WatchProviderIds.MAX -> {
                    val app = "${WatchProviderSchemes.HBO_MAX}${WatchProviderPaths.HBO_SEARCH}$encodedTitle"
                    val web = "${WatchProviderPaths.MAX_WEB_SEARCH}$encodedTitle"
                    app to web
                }

                else -> null
            }

        return WatchProviderUrls(
            appUrl = providerUrl?.first,
            webUrl = providerUrl?.second,
            tmdbUrl = tmdbFallbackUrl ?: "",
        )
    }
}
