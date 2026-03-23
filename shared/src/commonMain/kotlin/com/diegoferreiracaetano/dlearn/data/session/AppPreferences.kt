@file:OptIn(com.russhwolf.settings.ExperimentalSettingsApi::class)

package com.diegoferreiracaetano.dlearn.data.session

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import com.russhwolf.settings.coroutines.FlowSettings

interface AppPreferences {
    val language: Flow<String?>
    val country: Flow<String?>
    val notificationsEnabled: Flow<Boolean>

    fun getLanguageSync(): String?
    fun getCountrySync(): String?

    suspend fun saveLanguage(language: String?)
    suspend fun saveCountry(country: String?)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun clear()
}

class SettingsAppPreferences(
    private val settings: Settings
) : AppPreferences {
    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    companion object {
        private const val KEY_LANGUAGE = "pref_language"
        private const val KEY_COUNTRY = "pref_country"
        private const val KEY_NOTIFICATIONS = "pref_notifications"
    }

    override val language: Flow<String?> = flowSettings.getStringOrNullFlow(KEY_LANGUAGE)
    
    override val country: Flow<String?> = flowSettings.getStringOrNullFlow(KEY_COUNTRY)
    
    override val notificationsEnabled: Flow<Boolean> = flowSettings.getBooleanFlow(KEY_NOTIFICATIONS, true)

    override fun getLanguageSync(): String? = settings.getStringOrNull(KEY_LANGUAGE)
    
    override fun getCountrySync(): String? = settings.getStringOrNull(KEY_COUNTRY)

    override suspend fun saveLanguage(language: String?) {
        if (language == null) {
            settings.remove(KEY_LANGUAGE)
        } else {
            settings.putString(KEY_LANGUAGE, language)
        }
    }

    override suspend fun saveCountry(country: String?) {
        if (country == null) {
            settings.remove(KEY_COUNTRY)
        } else {
            settings.putString(KEY_COUNTRY, country)
        }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settings.putBoolean(KEY_NOTIFICATIONS, enabled)
    }

    override suspend fun clear() {
        settings.remove(KEY_LANGUAGE)
        settings.remove(KEY_COUNTRY)
        settings.remove(KEY_NOTIFICATIONS)
    }
}
