package com.diegoferreiracaetano.dlearn

import android.os.Build
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    override val language: String get() = Locale.getDefault().toLanguageTag()
    override val appVersion: String = AppConstants.APP_VERSION
    override val deviceModel: String = "${Build.MANUFACTURER}${Constants.SPACE}${Build.MODEL}"

    override fun updateLocale(
        language: String,
        country: String,
    ) {
        val locale =
            when {
                language.contains(Constants.DASH) -> Locale.forLanguageTag(language)
                country.isNotEmpty() -> Locale.Builder().setLanguage(language).setRegion(country).build()
                else -> Locale.Builder().setLanguage(language).build()
            }
        Locale.setDefault(locale)
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val currentPlatform: PlatformType = PlatformType.Android
