package com.diegoferreiracaetano.dlearn

import android.os.Build
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    override val language: String = Locale.getDefault().toLanguageTag()
    override val appVersion: String = "1.0.0" // Idealmente injetado via BuildConfig
    override val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val currentPlatform: PlatformType = PlatformType.Android