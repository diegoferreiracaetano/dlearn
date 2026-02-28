package com.diegoferreiracaetano.dlearn.domain.home

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(type: HomeFilterType = HomeFilterType.ALL, search: String? = null): Flow<Home>
}
