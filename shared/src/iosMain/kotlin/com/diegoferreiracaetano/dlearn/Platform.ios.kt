package com.diegoferreiracaetano.dlearn

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val language: String get() = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"
    override val appVersion: String = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "1.0.0"
    override val deviceModel: String = UIDevice.currentDevice.model

    override fun updateLocale(
        language: String,
        country: String,
    ) {
        val localeTag = if (country.isNotEmpty()) "$language-$country" else language
        NSUserDefaults.standardUserDefaults.setObject(listOf(localeTag), "AppleLanguages")
        NSUserDefaults.standardUserDefaults.synchronize()
    }
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val currentPlatform: PlatformType = PlatformType.IOS
