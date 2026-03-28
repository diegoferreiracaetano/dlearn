package com.diegoferreiracaetano.dlearn.domain.movie

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun execute(request: AppRequest): Flow<Screen>
}
