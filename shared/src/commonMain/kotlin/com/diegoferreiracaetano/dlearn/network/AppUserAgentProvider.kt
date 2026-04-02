package com.diegoferreiracaetano.dlearn.network

import com.diegoferreiracaetano.dlearn.Platform
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository

class AppUserAgentProvider(
    private val platform: Platform,
    private val preferencesRepository: PreferencesRepository,
) {
    fun get(): AppUserAgent =
        AppUserAgent(
            appName = "DLearn",
            appVersion = platform.appVersion,
            deviceName = platform.name,
        )

    fun getLanguage(): String = preferencesRepository.language

    fun getCountry(): String = preferencesRepository.country
}
