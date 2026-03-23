package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import java.util.Locale
import java.util.ResourceBundle

open class I18nProvider {
    open fun getString(key: AppStringType, language: String): String {
        // Normaliza o idioma: se vier en-US ou en_US, tenta usar o Locale correto
        val cleanLang = language.replace("-", "_")
        val locale = try {
            if (cleanLang.contains("_")) {
                val parts = cleanLang.split("_")
                Locale(parts[0], parts[1])
            } else {
                Locale(cleanLang)
            }
        } catch (e: Exception) {
            Locale.ENGLISH
        }
        
        return try {
            val bundle = ResourceBundle.getBundle("strings", locale)
            bundle.getString(key.name.lowercase())
        } catch (e: Exception) {
            // Fallback: Tenta apenas o idioma (ex: se en_US falhar, tenta en)
            try {
                val languageOnlyLocale = Locale(locale.language)
                val bundle = ResourceBundle.getBundle("strings", languageOnlyLocale)
                bundle.getString(key.name.lowercase())
            } catch (e2: Exception) {
                // Fallback final: Inglês
                try {
                    val bundle = ResourceBundle.getBundle("strings", Locale.ENGLISH)
                    bundle.getString(key.name.lowercase())
                } catch (e3: Exception) {
                    key.name
                }
            }
        }
    }
}
