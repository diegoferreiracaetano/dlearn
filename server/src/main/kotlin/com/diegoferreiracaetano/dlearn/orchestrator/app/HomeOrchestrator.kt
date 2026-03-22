package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class HomeOrchestrator(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val screenBuilder: HomeScreenBuilder
) : Orchestrator {
    private val homeCache = InMemoryCache<String, Screen>(5.minutes)

    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> = flow {
        val type = request.params?.get("type")?.let {
            runCatching { HomeFilterType.valueOf(it) }.getOrNull()
        } ?: HomeFilterType.ALL

        val cacheKey = "$userId-$appVersion-$lang-$type"
        val screen = homeCache.getOrPut(cacheKey) {
            val domainData = getHomeDataUseCase.execute(userId, type)
            screenBuilder.build(domainData, appVersion, lang, type)
        }
        emit(screen)
    }
}
