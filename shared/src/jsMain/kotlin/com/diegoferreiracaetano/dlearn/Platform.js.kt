package com.diegoferreiracaetano.dlearn

class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other