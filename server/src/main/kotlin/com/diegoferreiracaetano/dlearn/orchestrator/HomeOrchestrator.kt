package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlin.time.Duration.Companion.minutes

class HomeOrchestrator(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val screenBuilder: HomeScreenBuilder
) {
    private val homeCache = InMemoryCache<String, Screen>(5.minutes)

    suspend fun getHomeData(userId: String, appVersion: Int, lang: String): Screen {
        return homeCache.getOrPut("$userId-$appVersion-$lang") {
            val domainData = getHomeDataUseCase.execute(userId)
            screenBuilder.build(domainData, appVersion, lang)
        }
    }
}
