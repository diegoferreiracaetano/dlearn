package com.diegoferreiracaetano.dlearn

import kotlinx.browser.window

class JsPlatform : Platform {
    private var _customLanguage: String? = null

    override val name: String = "Web with Kotlin/JS"
    override val language: String get() = _customLanguage ?: window.navigator.language
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = window.navigator.userAgent

    override fun updateLocale(language: String, country: String) {
        val localeTag = if (country.isNotEmpty()) "${language}-$country" else language
        _customLanguage = localeTag
        // No navegador, não podemos mudar o idioma do sistema, mas atualizamos o estado interno 
        // para que as ViewModels consumam o idioma correto.
    }
}

actual fun getPlatform(): Platform = JsPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other
