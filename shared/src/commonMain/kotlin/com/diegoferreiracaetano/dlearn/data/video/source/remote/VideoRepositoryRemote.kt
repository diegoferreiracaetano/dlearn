package com.diegoferreiracaetano.dlearn.data.video.source.remote

import com.diegoferreiracaetano.dlearn.data.util.OrderType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepositoryRemote(
    private val dataSource: VideoNetworkDataSource,
) : VideoRepository {
    override fun list(
        search: String,
        category: String,
        order: OrderType
    ): Flow<List<Video>> = flow {
        emit(
            dataSource.fetchMovies().toDomain()
        )
    }

    override fun detail(id: String): Flow<Video> = flow {
        // TODO: implement network call
    }
}
