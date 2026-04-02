package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.LocaleConstants
import com.diegoferreiracaetano.dlearn.PreferenceConstants
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SettingsMapper(
    private val i18n: I18nProvider,
) {
    fun toNotificationRows(
        notificationsEnabled: Boolean,
        lang: String,
    ): List<Component> =
        listOf(
            AppSwitchRowComponent(
                title = i18n.getString(AppStringType.PUSH_NOTIFICATIONS, lang),
                subtitle = i18n.getString(AppStringType.PUSH_NOTIFICATIONS_DESC, lang),
                icon = AppIconType.NOTIFICATIONS,
                preferenceKey = PreferenceConstants.PREF_NOTIFICATIONS,
                isChecked = notificationsEnabled,
            ),
        )

    fun toLanguageRows(currentLang: String): List<Component> {
        val languages =
            listOf(
                LocaleConstants.LANG_PT_BR to AppStringType.LANGUAGE_PT_BR,
                LocaleConstants.LANG_EN_US to AppStringType.LANGUAGE_EN_US,
                LocaleConstants.LANG_ES_ES to AppStringType.LANGUAGE_ES_ES,
            )

        return languages.map { (code, type) ->
            AppSelectionRowComponent(
                title = i18n.getString(type, currentLang),
                preferenceKey = PreferenceConstants.PREF_LANGUAGE,
                value = code,
                isSelected = currentLang.trim().equals(code.trim(), ignoreCase = true),
            )
        }
    }

    fun toCountryRows(
        currentCountry: String?,
        lang: String,
    ): List<Component> {
        val countries =
            listOf(
                LocaleConstants.COUNTRY_BR to AppStringType.COUNTRY_BR,
                LocaleConstants.COUNTRY_US to AppStringType.COUNTRY_US,
                LocaleConstants.COUNTRY_ES to AppStringType.COUNTRY_ES,
            )

        return countries.map { (code, type) ->
            AppSelectionRowComponent(
                title = i18n.getString(type, lang),
                preferenceKey = PreferenceConstants.PREF_COUNTRY,
                value = code,
                isSelected = currentCountry?.trim().equals(code.trim(), ignoreCase = true),
            )
        }
    }
}
