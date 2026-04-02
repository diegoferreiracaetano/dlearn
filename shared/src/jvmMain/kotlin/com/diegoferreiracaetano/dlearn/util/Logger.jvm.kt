package com.diegoferreiracaetano.dlearn.util

class JVMLogger : Logger {
    override fun d(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        val logMessage = if (throwable != null) "$message: ${throwable.message}" else message
        println("KMP_LOG [$tag]: $logMessage")
        throwable?.printStackTrace()
    }
}

actual fun getLogger(): Logger = JVMLogger()
