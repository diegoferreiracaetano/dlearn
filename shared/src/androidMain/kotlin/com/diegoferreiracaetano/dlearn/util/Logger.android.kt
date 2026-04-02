package com.diegoferreiracaetano.dlearn.util

class AndroidLogger : Logger {
    override fun d(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        android.util.Log.d(tag, message, throwable)
    }
}

actual fun getLogger(): Logger = AndroidLogger()
