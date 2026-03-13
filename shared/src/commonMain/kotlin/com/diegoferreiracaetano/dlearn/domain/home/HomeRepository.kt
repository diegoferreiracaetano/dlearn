package com.diegoferreiracaetano.dlearn.domain.home

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(
        type: HomeFilterType = HomeFilterType.ALL,
        search: String? = null,
        categoryId: String? = null
    ): Flow<Screen>
}
