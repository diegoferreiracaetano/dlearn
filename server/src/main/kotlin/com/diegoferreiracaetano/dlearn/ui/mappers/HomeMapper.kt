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

    fun toCarousel(title: String, videos: List<Video>, showRank: Boolean = false): CarouselComponent {
        return CarouselComponent(
            title = title,
            items = videos.map { video -> 
                CardComponent(
                    title = video.title,
                    imageUrl = video.imageUrl
                )
            }
        )
    }

    fun toBannerCarousel(title: String, videos: List<Video>): CarouselComponent {
        return CarouselComponent(
            title = title,
            items = videos.map { video ->
                CardComponent(
                    title = video.title,
                    imageUrl = video.imageUrl
                )
            }
        )
    }
}
