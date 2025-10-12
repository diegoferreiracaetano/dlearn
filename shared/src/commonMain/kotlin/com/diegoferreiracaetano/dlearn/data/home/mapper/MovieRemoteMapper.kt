package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.data.home.model.MovieRemote
import com.diegoferreiracaetano.dlearn.domain.video.Video

internal fun MovieRemote.toVideo() = Video(
    id = this.id.toString(),
    title = this.title,
    subtitle = "", // MovieRemote has no subtitle
    description = this.overview,
    url = "", // MovieRemote has no url
    imageUrl = "https://image.tmdb.org/t/p/w500${posterPath.orEmpty()}",
    categories = emptyList(), // MovieRemote has no categories
    isFavorite = false, // MovieRemote has no isFavorite
    rating = 0f, // MovieRemote has no rating
    progress = 0f, // MovieRemote has no progress
    type = com.diegoferreiracaetano.dlearn.domain.video.VideoType.DEFAULT // Default type
)
