package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.UIConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import java.util.Locale

class HomeMapper(private val i18n: I18nProvider) {
    fun toBannerMain(video: Video): FullScreenBannerComponent =
        FullScreenBannerComponent(
            id = video.id,
            title = video.title,
            subtitle = video.subtitle,
            imageUrl = video.imageUrl,
            actionUrl = AppPath(AppNavigationRoute.MOVIES, mapOf(AppQueryParam.ID to video.id)),
        )

    fun toCarousel(
        title: String,
        videos: List<Video>,
        lang: String,
        showRank: Boolean = false,
    ): MovieCarouselComponent =
        MovieCarouselComponent(
            title = title,
            items =
                videos.mapIndexed { index, video ->
                    video.toMovieItemComponent(lang, rank = if (showRank) index + 1 else null)
                },
        )

    fun toBannerCarousel(
        title: String,
        videos: List<Video>,
        lang: String,
    ): BannerCarouselComponent =
        BannerCarouselComponent(
            title = title,
            items =
                videos.map { video ->
                    video.toMovieItemComponent(lang)
                },
        )

    private fun Video.toMovieItemComponent(
        lang: String,
        rank: Int? = null,
    ): MovieItemComponent =
        MovieItemComponent(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            rating = rating?.let { String.format(Locale.US, UIConstants.RATING_FORMAT, it) },
            year = subtitle,
            duration = null,
            contentRating = UIConstants.DEFAULT_CONTENT_RATING,
            genre = categories.firstOrNull()?.title,
            movieType =
                if (mediaType == MediaType.MOVIES) {
                    i18n.getString(AppStringType.FILTER_MOVIES, lang)
                } else {
                    i18n.getString(AppStringType.FILTER_SERIES, lang)
                },
            isPremium = false,
            isFavorite = isFavorite,
            rank = rank,
            actionUrl = AppPath(AppNavigationRoute.MOVIES, mapOf(AppQueryParam.ID to id)),
        )
}
