package com.diegoferreiracaetano.dlearn.domain.main

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getMain(
        type: HomeFilterType = HomeFilterType.ALL,
        search: String? = null
    ): Flow<Screen>

    fun getContent(
        type: HomeFilterType = HomeFilterType.ALL,
        search: String? = null
    ): Flow<Screen>
}
