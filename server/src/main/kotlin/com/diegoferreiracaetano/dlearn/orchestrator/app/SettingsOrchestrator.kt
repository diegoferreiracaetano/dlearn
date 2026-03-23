package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsOrchestrator(
    private val screenBuilder: SettingsScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        userAgent: AppUserAgent
    ): Flow<Screen> = flow {
        val path = NavigationRoutes.extractPath(request.path)
        
        when (path) {
            NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                emit(screenBuilder.buildNotificationScreen(userAgent.language))
            }
            NavigationRoutes.SETTINGS_LANGUAGE -> {
                emit(screenBuilder.buildLanguageScreen(userAgent.language))
            }
            NavigationRoutes.SETTINGS_COUNTRY -> {
                emit(screenBuilder.buildCountryScreen(userAgent.country, userAgent.language))
            }
            else -> throw IllegalArgumentException("Invalid settings path: $path")
        }
    }
}
