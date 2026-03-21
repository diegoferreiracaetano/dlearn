package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import java.util.*

open class I18nProvider {
    open fun getString(key: AppStringType, language: String): String {
        val locale = try {
            Locale.forLanguageTag(language)
        } catch (e: Exception) {
            Locale.ENGLISH
        }
        
        return try {
            val bundle = ResourceBundle.getBundle("strings", locale)
            bundle.getString(key.name.lowercase())
        } catch (e: Exception) {
            // Fallback to English if the specific language bundle is missing or key is missing
            try {
                val bundle = ResourceBundle.getBundle("strings", Locale.ENGLISH)
                bundle.getString(key.name.lowercase())
            } catch (e2: Exception) {
                key.name
            }
        }
    }
}
