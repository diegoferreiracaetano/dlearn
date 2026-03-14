package com.diegoferreiracaetano.dlearn.domain.main

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getMain(
        route: String,
        type: HomeFilterType = HomeFilterType.ALL,
        search: String? = null
    ): Flow<Screen>

    fun getContent(
        route: String,
        type: HomeFilterType = HomeFilterType.ALL,
        search: String? = null
    ): Flow<Screen>
}
