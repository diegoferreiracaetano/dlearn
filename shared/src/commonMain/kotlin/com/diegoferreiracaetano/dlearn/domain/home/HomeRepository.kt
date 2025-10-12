package com.diegoferreiracaetano.dlearn.domain.home

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHome(): Flow<Home>
}
