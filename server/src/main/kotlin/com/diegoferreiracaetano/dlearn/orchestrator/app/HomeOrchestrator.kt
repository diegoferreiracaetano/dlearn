package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.data.cache.CacheStrategy
import com.diegoferreiracaetano.dlearn.data.cache.toCache
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeOrchestrator(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val screenBuilder: HomeScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val type = request.params?.get("type")?.let {
            runCatching { HomeFilterType.valueOf(it) }.getOrNull()
        } ?: HomeFilterType.ALL

        val language = header.language
        val userId = header.userId
        val cacheKey = "home_${header.userAgent.appVersion}_${language}_${type}_$userId"

        return flow {
            val domainData = getHomeDataUseCase.execute(language, type, header)
            val screen = screenBuilder.build(domainData, language, type)
            emit(screen)
        }.toCache(
            key = cacheKey,
            strategy = CacheStrategy.CACHE_FIRST
        )
    }
}
