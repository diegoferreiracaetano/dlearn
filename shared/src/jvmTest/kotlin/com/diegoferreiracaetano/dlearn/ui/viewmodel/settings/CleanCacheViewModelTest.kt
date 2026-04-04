package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CleanCacheViewModelTest {

    private val preferencesRepository = mockk<PreferencesRepository>(relaxed = true)
    private val globalEventDispatcher = mockk<GlobalEventDispatcher>(relaxed = true)
    private val viewModel = CleanCacheViewModel(preferencesRepository, globalEventDispatcher)

    @Test
    fun `when confirmClearCache is called should clear preferences and dispatch navigation`() {
        viewModel.confirmClearCache()

        verify(exactly = 1) { preferencesRepository.clear() }
        verify(exactly = 1) { 
            globalEventDispatcher.tryEmit(match { it is GlobalEvent.Navigation && it.route == AppNavigationRoute.PROFILE })
        }
    }
}
