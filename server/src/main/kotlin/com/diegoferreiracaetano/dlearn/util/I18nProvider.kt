package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import java.util.Locale
import java.util.ResourceBundle

open class I18nProvider {

    open fun getString(
        key: AppStringType,
        language: String
    ): String {
        val locale = language.toLocale()
        return resolveString(key, locale)
            ?: resolveString(key, Locale(locale.language))
            ?: resolveString(key, Locale.ROOT)
            ?: key.name
    }

    private fun resolveString(
        key: AppStringType,
        locale: Locale
    ): String? {
        return runCatching {
            ResourceBundle
                .getBundle("strings", locale)
                .getString(key.name.lowercase())
        }.getOrNull()
    }
}

private fun String.toLocale(): Locale {
    val firstLanguage = split(",").firstOrNull() ?: this
    val clean = firstLanguage.split(";").first().trim().replace("-", "_")
    val parts = clean.split("_")

    return when (parts.size) {
        2 -> Locale(parts[0], parts[1])
        1 -> Locale(parts[0])
        else -> Locale.ENGLISH
    }
}
