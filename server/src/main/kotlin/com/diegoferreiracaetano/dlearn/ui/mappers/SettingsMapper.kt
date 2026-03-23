package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SettingsMapper(private val i18n: I18nProvider) {

    fun toNotificationRows(notificationsEnabled: Boolean, lang: String): List<Component> {
        return listOf(
            AppSwitchRowComponent(
                title = i18n.getString(AppStringType.PUSH_NOTIFICATIONS, lang),
                subtitle = i18n.getString(AppStringType.PUSH_NOTIFICATIONS_DESC, lang),
                icon = AppIconType.NOTIFICATIONS,
                preferenceKey = "pref_notifications",
                isChecked = notificationsEnabled
            )
        )
    }

    fun toLanguageRows(currentLang: String): List<Component> {
        val languages = listOf(
            "pt-BR" to AppStringType.LANGUAGE_PT_BR,
            "en-US" to AppStringType.LANGUAGE_EN_US,
            "es-ES" to AppStringType.LANGUAGE_ES_ES
        )

        return languages.map { (code, type) ->
            AppSelectionRowComponent(
                title = i18n.getString(type, currentLang),
                preferenceKey = "pref_language",
                value = code,
                isSelected = currentLang.trim().equals(code.trim(), ignoreCase = true)
            )
        }
    }

    fun toCountryRows(currentCountry: String?, lang: String): List<Component> {
        val countries = listOf(
            "BR" to AppStringType.COUNTRY_BR,
            "US" to AppStringType.COUNTRY_US,
            "ES" to AppStringType.COUNTRY_ES
        )

        return countries.map { (code, type) ->
            AppSelectionRowComponent(
                title = i18n.getString(type, lang),
                preferenceKey = "pref_country",
                value = code,
                isSelected = currentCountry?.trim().equals(code.trim(), ignoreCase = true)
            )
        }
    }
}
