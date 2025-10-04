package com.diegoferreiracaetano.dlearn

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform