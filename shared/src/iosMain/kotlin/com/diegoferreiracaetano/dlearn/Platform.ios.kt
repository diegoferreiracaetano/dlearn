package com.diegoferreiracaetano.dlearn

import platform.UIKit.UIDevice
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages
import platform.Foundation.NSBundle

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val language: String = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"
    override val appVersion: String = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "1.0.0"
    override val deviceModel: String = UIDevice.currentDevice.model
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val currentPlatform: PlatformType = PlatformType.IOS