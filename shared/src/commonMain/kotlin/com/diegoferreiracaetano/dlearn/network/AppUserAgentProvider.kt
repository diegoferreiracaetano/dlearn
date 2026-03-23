package com.diegoferreiracaetano.dlearn.network

import com.diegoferreiracaetano.dlearn.Platform
import com.diegoferreiracaetano.dlearn.data.session.AppPreferences

class AppUserAgentProvider(
    private val platform: Platform,
    private val prefs: AppPreferences
) {

    fun get(): AppUserAgent {
        return AppUserAgent(
            appName = "DLearn",
            appVersion = platform.appVersion,
            deviceName = platform.name,
            language = prefs.getLanguageSync() ?: "pt",
            country = prefs.getCountrySync() ?: "BR"
        )
    }
}