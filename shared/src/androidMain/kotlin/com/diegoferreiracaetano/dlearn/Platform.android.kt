package com.diegoferreiracaetano.dlearn

import android.os.Build
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    override val language: String get() = Locale.getDefault().toLanguageTag()
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"

    override fun updateLocale(
        language: String,
        country: String,
    ) {
        val locale =
            when {
                language.contains("-") -> Locale.forLanguageTag(language)
                country.isNotEmpty() -> Locale(language, country)
                else -> Locale(language)
            }
        Locale.setDefault(locale)
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val currentPlatform: PlatformType = PlatformType.Android
