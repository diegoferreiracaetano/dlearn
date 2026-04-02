package com.diegoferreiracaetano.dlearn.util

import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.create

class IosLogger : Logger {
    override fun d(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        val nsTag = NSString.create(string = tag)
        val nsMessage =
            NSString.create(string = if (throwable != null) "$message: ${throwable.message}" else message)
        NSLog("KMP_LOG - %@: %@", nsTag, nsMessage)
    }
}

actual fun getLogger(): Logger = IosLogger()
