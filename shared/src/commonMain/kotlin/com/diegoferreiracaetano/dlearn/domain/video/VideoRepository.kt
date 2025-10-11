package com.diegoferreiracaetano.dlearn.domain.video

import com.diegoferreiracaetano.dlearn.data.util.OrderType
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun list(
        search: String,
        category: String,
        order: OrderType,
    ): Flow<List<Video>>

    fun detail(id: String): Flow<Video>
}
