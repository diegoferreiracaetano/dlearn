package com.diegoferreiracaetano.dlearn

import kotlinx.browser.window

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
    override val language: String = window.navigator.language
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = window.navigator.userAgent
}

actual fun getPlatform(): Platform = JsPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other