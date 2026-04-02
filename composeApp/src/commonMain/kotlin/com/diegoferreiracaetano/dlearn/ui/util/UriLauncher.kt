package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.ui.platform.UriHandler

class UriResolutionException(
    message: String,
) : Exception(message)

object UriLauncher {
    fun openWithFallback(
        uriHandler: UriHandler,
        appUrl: String?,
        webUrl: String?,
        tmdbUrl: String?,
    ) {
        val urls = listOfNotNull(appUrl, webUrl, tmdbUrl)

        val success =
            urls.any { url ->
                try {
                    uriHandler.openUri(url)
                    true
                } catch (
                    @Suppress("TooGenericExceptionCaught") _: Exception,
                ) {
                    false
                }
            }

        if (!success) {
            throw UriResolutionException("Unable to open provider link")
        }
    }
}
