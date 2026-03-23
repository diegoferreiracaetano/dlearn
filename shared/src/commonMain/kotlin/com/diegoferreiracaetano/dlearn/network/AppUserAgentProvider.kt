package com.diegoferreiracaetano.dlearn.network

import com.diegoferreiracaetano.dlearn.Platform
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository

class AppUserAgentProvider(
    private val platform: Platform,
    private val preferencesRepository: PreferencesRepository
) {

    fun get(): AppUserAgent {
        val agent = AppUserAgent(
            appName = "DLearn",
            appVersion = platform.appVersion,
            deviceName = platform.name,
            language = preferencesRepository.language,
            country = preferencesRepository.country
        )
        println("DEBUG: AppUserAgentProvider.get() -> Language: ${agent.language}, Country: ${agent.country}")
        return agent
    }
}
