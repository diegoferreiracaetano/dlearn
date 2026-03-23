package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.flow

class SettingsOrchestrator(
    private val builder: SettingsScreenBuilder
): Orchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        userAgent: AppUserAgent
    ) = flow {
         val screen: Screen = when {
             request.path == NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                builder.buildNotificationScreen(userAgent.language)
            }

             request.path == NavigationRoutes.SETTINGS_LANGUAGE -> {
                builder.buildLanguageScreen(userAgent.language)
            }

            request.path == NavigationRoutes.SETTINGS_COUNTRY -> {
                builder.buildCountryScreen(userAgent.country, userAgent.language)
            }

            else -> builder.buildLanguageScreen(userAgent.language)
        }

        emit(screen)
    }
}
