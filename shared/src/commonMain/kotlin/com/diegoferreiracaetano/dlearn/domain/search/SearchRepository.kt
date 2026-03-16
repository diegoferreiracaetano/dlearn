package com.diegoferreiracaetano.dlearn.domain.search

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchMain(): Flow<Screen>
    fun search(query: String): Flow<Screen>
}
