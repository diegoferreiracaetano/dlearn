package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.mappers.SettingsMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SettingsScreenBuilder(
    private val mapper: SettingsMapper,
    private val i18n: I18nProvider
) {
    fun buildNotificationScreen(lang: String): Screen {
        val components = mutableListOf<Component>()
        
        components.add(
            AppTopBarComponent(
                title = i18n.getString(AppStringType.NOTIFICATION_TITLE, lang)
            )
        )
        
        components.addAll(mapper.toNotificationRows(lang))
        
        return Screen(components = components)
    }

    fun buildLanguageScreen(currentLang: String): Screen {
        val components = mutableListOf<Component>()
        
        components.add(
            AppTopBarComponent(
                title = i18n.getString(AppStringType.LANGUAGE_TITLE, currentLang)
            )
        )
        
        components.addAll(mapper.toLanguageRows(currentLang))
        
        return Screen(components = components)
    }

    fun buildCountryScreen(currentCountry: String?, lang: String): Screen {
        val components = mutableListOf<Component>()
        
        components.add(
            AppTopBarComponent(
                title = i18n.getString(AppStringType.COUNTRY_TITLE, lang)
            )
        )
        
        components.addAll(mapper.toCountryRows(currentCountry, lang))
        
        return Screen(components = components)
    }
}
