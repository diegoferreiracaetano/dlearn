package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import java.util.Locale
import java.util.ResourceBundle

open class I18nProvider {

    open fun getString(
        key: AppStringType,
        language: String
    ): String {
        return getRawString(key.name.lowercase(), language) ?: key.name
    }

    open fun getRawString(
        key: String,
        language: String
    ): String? {
        val locale = language.toLocale()
        
        // Tentamos resolver na ordem:
        // 1. Locale exato (ex: pt_BR)
        // 2. Idioma base (ex: pt)
        // 3. Root (strings.properties - que é nosso inglês)
        return resolveString(key, locale)
            ?: resolveString(key, Locale(locale.language))
            ?: resolveString(key, Locale.ROOT)
    }

    private fun resolveString(
        key: String,
        locale: Locale
    ): String? {
        return runCatching {
            // O segredo está no Control.NoFallbackControl para não pegar o locale da máquina
            val bundle = ResourceBundle.getBundle(
                "strings", 
                locale, 
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
            )
            if (bundle.containsKey(key)) bundle.getString(key) else null
        }.getOrNull()
    }
}

private fun String.toLocale(): Locale {
    if (this.isBlank()) return Locale.ROOT
    
    val firstLanguage = split(",").firstOrNull() ?: this
    val clean = firstLanguage.split(";").first().trim().replace("-", "_")
    val parts = clean.split("_")

    return when (parts.size) {
        2 -> Locale(parts[0], parts[1])
        1 -> Locale(parts[0])
        else -> Locale.ROOT
    }
}
