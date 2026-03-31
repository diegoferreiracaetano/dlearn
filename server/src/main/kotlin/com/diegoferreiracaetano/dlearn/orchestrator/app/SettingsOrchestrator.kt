package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
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
        header: AppHeader,
        userId: String
    ): Flow<Screen> = flow {
        val path = AppPath.parse(request.path).path
        val screen: Screen = when (path) {
            AppNavigationRoute.SETTINGS_NOTIFICATIONS -> {
                builder.buildNotificationScreen(header.notificationsEnabled, header.language)
            }

            AppNavigationRoute.SETTINGS_LANGUAGE -> {
                builder.buildLanguageScreen(header.language)
            }

            AppNavigationRoute.SETTINGS_COUNTRY -> {
                builder.buildCountryScreen(header.country, header.language)
            }

            else -> builder.buildLanguageScreen(header.language)
        }

        emit(screen)
    }
}
