package com.diegoferreiracaetano.dlearn

import com.diegoferreiracaetano.dlearn.data.session.AppPreferences

enum class PlatformType {
    Android,
    IOS,
    Other
}

interface Platform {
    val name: String
    val language: String // Idioma do sistema (device)
    val appVersion: String
    val deviceModel: String

    fun activeLanguage(prefs: AppPreferences): String = 
        prefs.getLanguageSync() ?: language

    fun activeCountry(prefs: AppPreferences): String = 
        prefs.getCountrySync() ?: "BR"

    fun userAgent(prefs: AppPreferences): String =
        "DLearn/$appVersion ($name; ${activeLanguage(prefs)}; ${activeCountry(prefs)})"
}

data class UserAgentContext(
    val appVersion: String = "1.0",
    val platform: String = "Unknown",
    val lang: String = "en",
    val country: String = "BR"
)

expect fun getPlatform(): Platform

expect val currentPlatform: PlatformType

val isIOS = currentPlatform == PlatformType.IOS

val isAndroid = currentPlatform == PlatformType.Android
