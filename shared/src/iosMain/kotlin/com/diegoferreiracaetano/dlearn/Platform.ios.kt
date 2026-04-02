package com.diegoferreiracaetano.dlearn

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + Constants.SPACE + UIDevice.currentDevice.systemVersion
    override val language: String
        get() = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: LocaleConstants.LANG_EN_US
    override val appVersion: String =
        NSBundle.mainBundle.infoDictionary?.get(BUNDLE) as? String ?: AppConstants.APP_VERSION
    override val deviceModel: String = UIDevice.currentDevice.model

    override fun updateLocale(
        language: String,
        country: String,
    ) {
        val localeTag = if (country.isNotEmpty()) "$language${Constants.DASH}$country" else language
        NSUserDefaults.standardUserDefaults.setObject(listOf(localeTag), APPLE_LANGUAGES_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }

    companion object {
        private const val APPLE_LANGUAGES_KEY = "AppleLanguages"
        private const val BUNDLE = "CFBundleShortVersionString"
    }
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val currentPlatform: PlatformType = PlatformType.IOS
