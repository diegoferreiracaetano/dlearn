package com.diegoferreiracaetano.dlearn

import java.util.Locale

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val language: String = Locale.getDefault().toLanguageTag()
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = System.getProperty("os.name") + " " + System.getProperty("os.version")
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other