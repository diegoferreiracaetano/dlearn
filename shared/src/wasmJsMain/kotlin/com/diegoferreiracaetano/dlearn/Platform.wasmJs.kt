package com.diegoferreiracaetano.dlearn

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val language: String = "en-US"
    override val appVersion: String = "1.0.0"
    override val deviceModel: String = "Web Browser"
    override fun updateLocale(language: String, country: String) {
        TODO("Not yet implemented")
    }
}

actual fun getPlatform(): Platform = WasmPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other
