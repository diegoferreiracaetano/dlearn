package com.diegoferreiracaetano.dlearn.data.app

import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class PreferencesRepositoryImpl(
    private val storage: KeyValueStorage
) : PreferencesRepository {

    companion object {
        private const val PREF_LANGUAGE = "pref_language"
        private const val PREF_COUNTRY = "pref_country"
        private const val PREF_NOTIFICATIONS = "pref_notifications"
        
        private const val DEFAULT_LANGUAGE = "pt-BR"
        private const val DEFAULT_COUNTRY = "BR"
        private const val DEFAULT_NOTIFICATIONS = true
    }

    override var language: String
        get() = storage.get(PREF_LANGUAGE, DEFAULT_LANGUAGE)
        set(value) {
            println("DEBUG: Saving Language: $value")
            storage.put(PREF_LANGUAGE, value)
        }

    override var country: String
        get() = storage.get(PREF_COUNTRY, DEFAULT_COUNTRY)
        set(value) {
            println("DEBUG: Saving Country: $value")
            storage.put(PREF_COUNTRY, value)
        }

    override var notificationsEnabled: Boolean
        get() = storage.get(PREF_NOTIFICATIONS, DEFAULT_NOTIFICATIONS)
        set(value) {
            println("DEBUG: Saving Notifications: $value")
            storage.put(PREF_NOTIFICATIONS, value)
        }

    override fun updatePreference(key: String, value: String) {
        println("DEBUG: updatePreference(key=$key, value=$value)")
        when (key) {
            PREF_LANGUAGE -> language = value
            PREF_COUNTRY -> country = value
            PREF_NOTIFICATIONS -> {
               notificationsEnabled = value.toBoolean()
            }
        }
    }

    override val onConfigurationChanged: Flow<Unit> = combine(
        storage.getFlow(PREF_LANGUAGE, DEFAULT_LANGUAGE),
        storage.getFlow(PREF_COUNTRY, DEFAULT_COUNTRY),
        storage.getFlow(PREF_NOTIFICATIONS, DEFAULT_NOTIFICATIONS)
    ) { l, c, n -> 
        println("DEBUG: Configuration Flow emitted: L=$l, C=$c, N=$n")
        listOf(l, c, n) 
    }
        .distinctUntilChanged()
        .map { }

    override fun clear() {
        storage.clear()
    }
}
