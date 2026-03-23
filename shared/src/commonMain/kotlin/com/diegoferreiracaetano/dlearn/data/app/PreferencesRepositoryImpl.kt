package com.diegoferreiracaetano.dlearn.data.app

import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PreferencesRepositoryImpl(private val settings: Settings) : PreferencesRepository {

    private val _onConfigurationChanged = MutableStateFlow(0L)
    override val onConfigurationChanged: StateFlow<Long> = _onConfigurationChanged.asStateFlow()

    override var language: String
        get() = settings.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE)
        set(value) {
            settings[KEY_LANGUAGE] = value
            notifyChange()
        }

    override var country: String
        get() = settings.getString(KEY_COUNTRY, DEFAULT_COUNTRY)
        set(value) {
            settings[KEY_COUNTRY] = value
            notifyChange()
        }

    override var notificationsEnabled: Boolean
        get() = settings.getBoolean(KEY_NOTIFICATIONS, DEFAULT_NOTIFICATIONS)
        set(value) {
            settings[KEY_NOTIFICATIONS] = value
            notifyChange()
        }

    override fun updatePreference(key: String, value: String) {
        when (key) {
            KEY_LANGUAGE -> language = value
            KEY_COUNTRY -> country = value
            KEY_NOTIFICATIONS -> notificationsEnabled = value.toBooleanStrictOrNull() ?: DEFAULT_NOTIFICATIONS
        }
    }

    override fun clear() {
        settings.clear()
        notifyChange()
    }

    private fun notifyChange() {
        _onConfigurationChanged.update { it + 1 }
    }

    companion object {
        const val KEY_LANGUAGE = "pref_language"
        const val KEY_COUNTRY = "pref_country"
        const val KEY_NOTIFICATIONS = "pref_notifications"

        const val DEFAULT_LANGUAGE = "pt-BR"
        const val DEFAULT_COUNTRY = "BR"
        const val DEFAULT_NOTIFICATIONS = true
    }
}
