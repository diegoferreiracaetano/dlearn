package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SettingsMapper(private val i18n: I18nProvider) {

    fun toNotificationRows(lang: String): List<Component> {
        return listOf(
            AppSwitchRowComponent(
                title = i18n.getString(AppStringType.PUSH_NOTIFICATIONS, lang),
                subtitle = i18n.getString(AppStringType.PUSH_NOTIFICATIONS_DESC, lang),
                icon = AppIconType.NOTIFICATIONS,
                preferenceKey = "pref_notifications"
            )
        )
    }

    fun toLanguageRows(currentLang: String): List<Component> {
        val languages = listOf(
            "pt-BR" to "Português (Brasil)",
            "en-US" to "English (United States)",
            "es-ES" to "Español (España)"
        )

        return languages.map { (code, name) ->
            // Comparação ignorando case e espaços para evitar desvios no Header
            val isSelected = currentLang.trim().equals(code.trim(), ignoreCase = true)
            println("DEBUG: SettingsMapper - Comparison [${code.trim()}] == [${currentLang.trim()}] -> $isSelected")
            
            AppSelectionRowComponent(
                title = name,
                preferenceKey = "pref_language",
                value = code,
                isSelected = isSelected
            )
        }
    }

    fun toCountryRows(currentCountry: String?, lang: String): List<Component> {
        val countries = listOf(
            "BR" to "Brasil",
            "US" to "United States",
            "ES" to "España"
        )

        return countries.map { (code, name) ->
            val isSelected = currentCountry?.trim().equals(code.trim(), ignoreCase = true)
            println("DEBUG: SettingsMapper - Comparison [${code.trim()}] == [${currentCountry?.trim()}] -> $isSelected")

            AppSelectionRowComponent(
                title = name,
                preferenceKey = "pref_country",
                value = code,
                isSelected = isSelected
            )
        }
    }
}
