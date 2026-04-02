package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.UIConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import java.util.Locale

class VideoMapper(private val i18n: I18nProvider) {
    fun toMovieItem(
        video: Video,
        lang: String,
    ): MovieItemComponent =
        MovieItemComponent(
            id = video.id,
            title = video.title,
            subtitle = video.subtitle,
            imageUrl = video.imageUrl,
            rating = video.rating?.let { String.format(Locale.US, UIConstants.RATING_FORMAT, it) },
            year = video.subtitle,
            duration = null,
            contentRating = UIConstants.DEFAULT_CONTENT_RATING,
            genre = video.categories.firstOrNull()?.title,
            movieType =
            if (video.mediaType == MediaType.MOVIES) {
                i18n.getString(AppStringType.FILTER_MOVIES, lang)
            } else {
                i18n.getString(AppStringType.FILTER_SERIES, lang)
            },
            actionUrl = AppPath(AppNavigationRoute.MOVIES, mapOf(AppQueryParam.ID to video.id)),
        )

    fun toMovieItemComponents(
        videos: List<Video>,
        lang: String,
    ): List<Component> = videos.map { toMovieItem(it, lang) as Component }
}
