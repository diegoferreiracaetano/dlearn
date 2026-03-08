package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlin.time.Duration.Companion.minutes

class ProfileOrchestrator(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val screenBuilder: ProfileScreenBuilder
) {
    private val profileCache = InMemoryCache<String, Screen>(5.minutes)

    suspend fun getProfileData(userId: String, appVersion: Int, lang: String): Screen {
        return profileCache.getOrPut("$userId-$appVersion-$lang") {
            val domainData = getProfileDataUseCase.execute(userId)
            screenBuilder.build(domainData, appVersion, lang)
        }
    }
}
