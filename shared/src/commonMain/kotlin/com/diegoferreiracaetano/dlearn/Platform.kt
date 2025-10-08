package com.diegoferreiracaetano.dlearn

enum class PlatformType {
    Android,
    IOS,
    Other
}

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val currentPlatform: PlatformType

val isIOS = currentPlatform == PlatformType.IOS