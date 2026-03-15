package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

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
                video.toCardComponent(rank = if (showRank) index + 1 else null)
            }
        )
    }

    fun toBannerCarousel(title: String, videos: List<Video>): BannerCarouselComponent {
        return BannerCarouselComponent(
            title = title,
            items = videos.map { video ->
                video.toCardComponent()
            }
        )
    }

    private fun Video.toCardComponent(rank: Int? = null): CardComponent {
        return CardComponent(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            rating = rating?.toString(),
            year = "",
            duration = "",
            contentRating = "",
            genre = categories.firstOrNull()?.title ?: "",
            movieType = type.name,
            isPremium = false,
            rank = rank,
            actionUrl = "/video/$id"
        )
    }
}
