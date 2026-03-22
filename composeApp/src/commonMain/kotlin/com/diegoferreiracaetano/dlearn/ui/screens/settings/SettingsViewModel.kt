package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.data.session.AppPreferences
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.collectIn
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: AppRepository,
    private val preferences: AppPreferences,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _showClearCacheDialog = MutableStateFlow(false)
    val showClearCacheDialog = _showClearCacheDialog.asStateFlow()

    private var lastRequest: AppRequest? = null

    init {
        // Re-fetch content when any relevant preference changes to ensure localized and updated UI
        combine(
            preferences.language,
            preferences.country,
            preferences.notificationsEnabled
        ) { _, _, _ -> }
            .drop(1) // Skip initial emission
            .onEach { retry() }
            .launchIn(viewModelScope)
    }

    fun loadContent(path: String) {
        execute(AppRequest(path))
    }

    fun retry() {
        lastRequest?.let(::execute)
    }

    fun handleAction(action: String) {
        when {
            action.startsWith("pref_") -> handlePreferenceAction(action)
            action == "clear_cache" -> _showClearCacheDialog.value = true
            else -> execute(AppRequest(action))
        }
    }

    fun confirmClearCache() {
        viewModelScope.launch {
            preferences.clear()
            sessionManager.logout()
            _showClearCacheDialog.value = false
        }
    }

    fun dismissClearCacheDialog() {
        _showClearCacheDialog.value = false
    }

    private fun handlePreferenceAction(action: String) {
        val parts = action.split(":", limit = 2)
        if (parts.size < 2) return
        
        val key = parts[0]
        val value = parts[1]
        
        viewModelScope.launch {
            when (key) {
                "pref_language" -> preferences.saveLanguage(value)
                "pref_country" -> preferences.saveCountry(value)
                "pref_notifications" -> preferences.setNotificationsEnabled(value.toBoolean())
            }
        }
    }

    private fun execute(request: AppRequest) {
        lastRequest = request
        repository.execute(request.path, request.params, request.metadata)
            .collectIn(viewModelScope, _uiState)
    }
}
