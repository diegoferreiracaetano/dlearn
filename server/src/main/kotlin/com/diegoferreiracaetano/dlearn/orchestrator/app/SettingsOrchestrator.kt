package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsOrchestrator(
    private val builder: SettingsScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> = flow {
        val screen: Screen = when {
            request.path == NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                builder.buildNotificationScreen(header.notificationsEnabled, header.language)
            }

            request.path == NavigationRoutes.SETTINGS_LANGUAGE -> {
                builder.buildLanguageScreen(header.language)
            }

            request.path == NavigationRoutes.SETTINGS_COUNTRY -> {
                builder.buildCountryScreen(header.country, header.language)
            }

            else -> builder.buildLanguageScreen(header.language)
        }

        emit(screen)
    }
}
