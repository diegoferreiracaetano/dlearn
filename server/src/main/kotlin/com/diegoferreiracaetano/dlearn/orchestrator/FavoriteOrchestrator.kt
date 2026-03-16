package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.FavoriteScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase

class FavoriteOrchestrator(
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val getHomeDataUseCase: GetHomeDataUseCase
) {
    suspend fun getFavorite(userId: String, lang: String): Screen {
        // In a real app, this would fetch from a database or service
        // Reusing GetHomeDataUseCase just to have some data for now
        val data = getHomeDataUseCase.execute(userId)
        return favoriteScreenBuilder.build(data, lang)
    }

    suspend fun toggleFavorite(userId: String, movieId: String, lang: String): Screen {
        // Logic to add/remove from favorite
        val data = getHomeDataUseCase.execute(userId)
        return favoriteScreenBuilder.build(data, lang)
    }
}
