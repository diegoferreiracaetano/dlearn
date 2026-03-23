package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.AppRequestContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsOrchestrator(
    private val screenBuilder: SettingsScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        userAgent: String
    ): Flow<Screen> = flow {
        val context = AppRequestContext.fromUserAgent(userAgent)
        val path = NavigationRoutes.extractPath(request.path)
        
        when (path) {
            NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                emit(screenBuilder.buildNotificationScreen(context.lang))
            }
            NavigationRoutes.SETTINGS_LANGUAGE -> {
                emit(screenBuilder.buildLanguageScreen(context.lang))
            }
            NavigationRoutes.SETTINGS_COUNTRY -> {
                // Usa o país vindo do contexto (User-Agent) em vez do perfil
                emit(screenBuilder.buildCountryScreen(context.country, context.lang))
            }
            else -> throw IllegalArgumentException("Invalid settings path: $path")
        }
    }
}
