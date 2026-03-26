package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import java.util.Locale

class HomeMapper(private val i18n: I18nProvider) {
    fun toBannerMain(video: Video): FullScreenBannerComponent {
        return FullScreenBannerComponent(
            id = video.id,
            title = video.title,
            subtitle = video.subtitle,
            imageUrl = video.imageUrl
        )
    }

    fun toCarousel(title: String, videos: List<Video>, showRank: Boolean = false): MovieCarouselComponent {
        return MovieCarouselComponent(
            title = title,
            items = videos.mapIndexed { index, video ->
                video.toMovieItemComponent(rank = if (showRank) index + 1 else null)
            }
        )
    }

    fun toBannerCarousel(title: String, videos: List<Video>): BannerCarouselComponent {
        return BannerCarouselComponent(
            title = title,
            items = videos.map { video ->
                video.toMovieItemComponent()
            }
        )
    }

    private fun Video.toMovieItemComponent(rank: Int? = null): MovieItemComponent {
        return MovieItemComponent(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            rating = rating?.let { String.format(Locale.US, "%.1f", it) },
            year = subtitle,
            duration = null,
            contentRating = "L",
            genre = categories.firstOrNull()?.title,
            movieType = if (mediaType == MediaType.MOVIE) "Filme" else "Série",
            isPremium = false,
            rank = rank,
            actionUrl = AppPath(AppNavigationRoute.MOVIES, mapOf(AppQueryParam.ID to id))
        )
    }
}
