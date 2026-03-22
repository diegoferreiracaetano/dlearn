package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsOrchestrator(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val screenBuilder: SettingsScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> = flow {
        val path = NavigationRoutes.extractPath(request.path)
        
        when (path) {
            NavigationRoutes.SETTINGS_NOTIFICATIONS -> {
                emit(screenBuilder.buildNotificationScreen(lang))
            }
            NavigationRoutes.SETTINGS_LANGUAGE -> {
                emit(screenBuilder.buildLanguageScreen(lang))
            }
            NavigationRoutes.SETTINGS_COUNTRY -> {
                val profile = getProfileDataUseCase.execute(userId)
                emit(screenBuilder.buildCountryScreen(profile.country, lang))
            }
            else -> throw IllegalArgumentException("Invalid settings path: $path")
        }
    }
}
