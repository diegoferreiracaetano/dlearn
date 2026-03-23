package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.mappers.SettingsMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import com.diegoferreiracaetano.dlearn.util.toAppContainerComponent

class SettingsScreenBuilder(
    private val mapper: SettingsMapper,
    private val i18n: I18nProvider
) {
    fun buildNotificationScreen(notificationsEnabled: Boolean, lang: String): Screen {
        val components = mutableListOf<Component>()

        val topbar = AppTopBarComponent(
            title = i18n.getString(AppStringType.NOTIFICATION_TITLE, lang)
        )

        components.addAll(mapper.toNotificationRows(notificationsEnabled, lang))

        return  components.toAppContainerComponent(topbar)
    }

    fun buildLanguageScreen(currentLang: String): Screen {
        val components = mutableListOf<Component>()

        val topbar = AppTopBarComponent(
            title = i18n.getString(AppStringType.LANGUAGE_TITLE, currentLang)
        )
        
        components.addAll(mapper.toLanguageRows(currentLang))

        return  components.toAppContainerComponent(topbar)
    }

    fun buildCountryScreen(currentCountry: String?, lang: String): Screen {
        val components = mutableListOf<Component>()

        val topbar = AppTopBarComponent(
            title = i18n.getString(AppStringType.COUNTRY_TITLE, lang)
        )
        
        components.addAll(mapper.toCountryRows(currentCountry, lang))

        return  components.toAppContainerComponent(topbar)
    }
}
