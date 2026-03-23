package com.diegoferreiracaetano.dlearn.domain.app

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    var language: String
    var country: String
    var notificationsEnabled: Boolean
    
    fun updatePreference(key: String, value: String)

    val onConfigurationChanged: Flow<Unit>
    fun clear()
}
