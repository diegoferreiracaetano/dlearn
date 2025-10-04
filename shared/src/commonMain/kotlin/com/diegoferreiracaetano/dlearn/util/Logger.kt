package com.diegoferreiracaetano.dlearn.util

interface Logger {
    fun d(tag: String, message: String)
}
//
//expect fun getLogger(): Logger