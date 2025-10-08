package com.diegoferreiracaetano.dlearn

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual val currentPlatform: PlatformType = PlatformType.Other