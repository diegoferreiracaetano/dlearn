package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SettingsOrchestrator(
    private val builder: SettingsScreenBuilder
) {

    fun getNotificationScreen(userAgent: String): Screen {
        val agent = AppUserAgent.fromHeader(userAgent)
        return builder.buildNotificationScreen(agent.language)
    }

    fun getLanguageScreen(userAgent: String): Screen {
        val agent = AppUserAgent.fromHeader(userAgent)
        return builder.buildLanguageScreen(agent.language)
    }

    fun getCountryScreen(userAgent: String): Screen {
        val agent = AppUserAgent.fromHeader(userAgent)
        return builder.buildCountryScreen(agent.country, agent.language)
    }

    fun execute(path: String, userAgent: AppUserAgent) = flow<Screen> {
         val screen: Screen = when {
            path == NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                builder.buildNotificationScreen(userAgent.language)
            }

            path == NavigationRoutes.SETTINGS_LANGUAGE -> {
                builder.buildLanguageScreen(userAgent.language)
            }

            path == NavigationRoutes.SETTINGS_COUNTRY -> {
                builder.buildCountryScreen(userAgent.country, userAgent.language)
            }

            else -> builder.buildLanguageScreen(userAgent.language)
        }

        emit(screen)
    }
}
