package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.ui.sdui.*

class HomeMapper {
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
            rating = rating.toString(),
            year = "", // TODO: Add year to Video domain if available
            duration = "", // TODO: Add duration to Video domain if available
            contentRating = "", // TODO: Add contentRating to Video domain if available
            genre = categories.firstOrNull()?.title ?: "",
            type = type.name,
            isPremium = false,
            rank = rank,
            actionUrl = "/video/$id"
        )
    }
}
