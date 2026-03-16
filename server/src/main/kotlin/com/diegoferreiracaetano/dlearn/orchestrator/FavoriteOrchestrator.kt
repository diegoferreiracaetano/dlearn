package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.FavoriteScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.UserDataService

class FavoriteOrchestrator(
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val userDataService: UserDataService
) {
    suspend fun getFavorite(userId: String, lang: String): Screen {
        // In a real app, this would fetch from a database using IDs from userDataService
        val favoriteIds = userDataService.getFavorites(userId)
        // For now, we reuse GetHomeDataUseCase just to have some data, but in the future we'd filter by favoriteIds
        val data = getHomeDataUseCase.execute(userId)
        return favoriteScreenBuilder.build(data, lang)
    }

    suspend fun toggleFavorite(userId: String, movieId: String, lang: String): Screen {
        userDataService.toggleFavorite(userId, movieId)
        return getFavorite(userId, lang)
    }
}
