package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.TmdbConstants.IMAGE_BASE_URL
import com.diegoferreiracaetano.dlearn.TmdbConstants.IMAGE_W500
import com.diegoferreiracaetano.dlearn.data.home.model.MovieRemote
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoType

internal fun MovieRemote.toVideo() =
    Video(
        id = this.id.toString(),
        title = this.title,
        subtitle = "",
        description = this.overview,
        url = "",
        imageUrl = if (posterPath != null) "${IMAGE_BASE_URL}${IMAGE_W500}$posterPath" else "",
        categories = emptyList(),
        isFavorite = false,
        rating = 0f,
        progress = 0f,
        type = VideoType.DEFAULT,
    )
