package com.diegoferreiracaetano.dlearn.network

import kotlinx.serialization.Serializable

@Serializable
data class AppUserAgent(
    val appName: String,
    val appVersion: String,
    val deviceName: String,
) {
    fun toHeader(): String = "$appName/$appVersion ($deviceName)"

    companion object {
        fun fromHeader(header: String?): AppUserAgent {
            if (header.isNullOrBlank()) return AppUserAgent("DLearn", "1.0.0", "Unknown")
            return try {
                val appPart = header.substringBefore(" ")
                val deviceName = header.substringAfter("(", "").substringBeforeLast(")", "Unknown").substringBefore(";")
                val (appName, appVersion) = if (appPart.contains("/")) appPart.split("/") else listOf(appPart, "1.0.0")
                AppUserAgent(appName, appVersion, deviceName.trim())
            } catch (e: Exception) {
                AppUserAgent("DLearn", "1.0.0", "Unknown")
            }
        }
    }
}
