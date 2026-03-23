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
            ?: resolveString(key, Locale.ROOT) // Força o uso do strings.properties (base)
            ?: key.name
    }

    private fun resolveString(
        key: AppStringType,
        locale: Locale
    ): String? {
        return runCatching {
            // Usamos ResourceBundle.Control para evitar que o Java pegue o Locale padrão da máquina (System Locale)
            val control = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            ResourceBundle
                .getBundle("strings", locale, control)
                .getString(key.name.lowercase())
        }.getOrNull()
    }
}

private fun String.toLocale(): Locale {
    val clean = replace("-", "_")
    val parts = clean.split("_")

    return when (parts.size) {
        2 -> Locale(parts[0], parts[1])
        1 -> Locale(parts[0])
        else -> Locale.ENGLISH
    }
}
