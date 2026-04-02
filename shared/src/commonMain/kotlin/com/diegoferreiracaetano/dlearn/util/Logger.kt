package com.diegoferreiracaetano.dlearn.util

interface Logger {
    fun d(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    )
}

expect fun getLogger(): Logger
