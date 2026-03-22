package com.diegoferreiracaetano.dlearn.ui.screens.app

import com.diegoferreiracaetano.dlearn.data.session.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Handler centralizado para ações específicas de negócio dentro do fluxo SDUI.
 * Isso mantém o AppViewModel genérico para qualquer tela dinâmica.
 */
class AppActionHandler(
    private val preferences: AppPreferences
) {
    fun handleAction(scope: CoroutineScope, action: String): Boolean {
        return when {
            action.startsWith("pref_") -> {
                handlePreferenceAction(scope, action)
                true
            }
            action == "clear_cache" -> {
                scope.launch { preferences.clear() }
                true
            }
            else -> false
        }
    }

    private fun handlePreferenceAction(scope: CoroutineScope, action: String) {
        val parts = action.split(":", limit = 2)
        if (parts.size < 2) return
        
        val key = parts[0]
        val value = parts[1]
        
        scope.launch {
            when (key) {
                "pref_language" -> preferences.saveLanguage(value)
                "pref_country" -> preferences.saveCountry(value)
                "pref_notifications" -> preferences.setNotificationsEnabled(value.toBoolean())
            }
        }
    }
}
