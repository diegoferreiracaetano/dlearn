package com.diegoferreiracaetano.dlearn.network

data class AppUserAgent(
    val appName: String,
    val appVersion: String,
    val deviceName: String,
    val language: String,
    val country: String
) {

    fun toHeader(): String {
        return "$appName/$appVersion ($deviceName; $language; $country)"
    }

    companion object {

        fun fromHeader(header: String): AppUserAgent {
            return try {
                if (header.isBlank()) return AppUserAgent("DLearn", "1.0.0", "Unknown", "pt-BR", "BR")

                val appPart = header.substringBefore(" ")
                val infoPart = header.substringAfter("(").substringBeforeLast(")")

                val (appName, appVersion) = if (appPart.contains("/")) appPart.split("/") else listOf(appPart, "1.0.0")
                val parts = infoPart.split(";").map { it.trim() }

                AppUserAgent(
                    appName = appName,
                    appVersion = appVersion,
                    deviceName = parts.getOrNull(0) ?: "",
                    language = parts.getOrNull(1) ?: "pt-BR",
                    country = parts.getOrNull(2) ?: "BR"
                )
            } catch (e: Exception) {
                AppUserAgent("DLearn", "1.0.0", "Unknown", "pt-BR", "BR")
            }
        }
    }
}
