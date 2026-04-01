package com.diegoferreiracaetano.dlearn

import java.util.Locale

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val language: String get() = Locale.getDefault().toLanguageTag()
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = System.getProperty("os.name") + " " + System.getProperty("os.version")

    override fun updateLocale(language: String, country: String) {
        val locale = when {
            language.contains("-") -> Locale.forLanguageTag(language)
            country.isNotEmpty() -> Locale.Builder().setLanguage(language).setRegion(country).build()
            else -> Locale.Builder().setLanguage(language).build()
        }
        Locale.setDefault(locale)
    }
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other
