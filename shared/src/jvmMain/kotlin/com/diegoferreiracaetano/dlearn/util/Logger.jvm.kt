package com.diegoferreiracaetano.dlearn.util


class JSLogger : Logger {
    override fun d(tag: String, message: String) {
        println("KMP_LOG [$tag]: $message")
    }
}

actual fun getLogger(): Logger = JSLogger()