package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import java.util.Locale

class VideoMapper {
    fun toMovieItem(video: Video): MovieItemComponent {
        return MovieItemComponent(
            id = video.id,
            title = video.title,
            subtitle = video.subtitle,
            imageUrl = video.imageUrl,
            rating = video.rating?.let { String.format(Locale.US, "%.1f", it) },
            year = video.subtitle,
            duration = null,
            contentRating = "L",
            genre = video.categories.firstOrNull()?.title,
            movieType = if (video.mediaType == MediaType.MOVIES) "Filme" else "Série",
            actionUrl = AppPath(AppNavigationRoute.MOVIES, mapOf(AppQueryParam.ID to video.id))
        )
    }

    fun toMovieItemComponents(videos: List<Video>): List<Component> {
        return videos.map { toMovieItem(it) as Component }
    }
}
