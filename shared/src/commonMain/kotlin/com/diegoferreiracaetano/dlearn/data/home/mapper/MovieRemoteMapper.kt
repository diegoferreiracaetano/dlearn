package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.data.home.model.MovieRemote
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoType

internal fun MovieRemote.toVideo() = Video(
    id = this.id.toString(),
    title = this.title,
    subtitle = "",
    description = this.overview,
    url = "",
    imageUrl = "https://image.tmdb.org/t/p/w500${posterPath.orEmpty()}",
    categories = emptyList(),
    isFavorite = false,
    rating = 0f,
    progress = 0f,
    type = VideoType.DEFAULT
)
