package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.TmdbConstants
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
        imageUrl = if (posterPath != null) "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}$posterPath" else "",
        categories = emptyList(),
        isFavorite = false,
        rating = 0f,
        progress = 0f,
        type = VideoType.DEFAULT,
    )
