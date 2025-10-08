package com.diegoferreiracaetano.dlearn

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val currentPlatform: PlatformType = PlatformType.Android