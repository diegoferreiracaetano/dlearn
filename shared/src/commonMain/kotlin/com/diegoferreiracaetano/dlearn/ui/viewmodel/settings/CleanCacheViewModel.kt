package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent.Navigation
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher

class CleanCacheViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val globalEventDispatcher: GlobalEventDispatcher
) : ViewModel() {

    fun confirmClearCache() {
        preferencesRepository.clear()
        globalEventDispatcher.tryEmit(Navigation(AppNavigationRoute.PROFILE))
    }
}
