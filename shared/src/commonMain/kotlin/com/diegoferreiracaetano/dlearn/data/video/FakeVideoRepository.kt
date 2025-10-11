package com.diegoferreiracaetano.dlearn.data.video

import com.diegoferreiracaetano.dlearn.data.util.OrderType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.domain.video.VideoRepository
import com.diegoferreiracaetano.dlearn.domain.video.VideoType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeVideoRepository : VideoRepository {

    private val videos = listOf(
        Video(
            id = "1",
            title = "Introduction to Jetpack Compose",
            subtitle = "Jetpack Compose",
            description = "A comprehensive guide to Jetpack Compose for beginners.",
            categories = listOf(VideoCategory.JETPACK_COMPOSE, VideoCategory.ANDROID),
            imageUrl = "https://i3.ytimg.com/vi/n2t5_qA1Q-o/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.5f,
            progress = 10f,
            url = "https://www.youtube.com/watch?v=n2t5_qA1Q-o"
        ),
        Video(
            id = "2",
            title = "State Management in Compose",
            subtitle = "Jetpack Compose",
            description = "Learn how to manage state effectively in your Compose applications.",
            categories = listOf(VideoCategory.JETPACK_COMPOSE, VideoCategory.ANDROID),
            imageUrl = "https://i3.ytimg.com/vi/N_9o_L4nN5E/maxresdefault.jpg",
            isFavorite = true,
            rating = 4.8f,
            type = VideoType.BANNER,
            url = "https://www.youtube.com/watch?v=N_9o_L4nN5E"
        ),
        Video(
            id = "3",
            title = "Dagger Hilt for Dependency Injection",
            subtitle = "Android",
            description = "Master dependency injection in Android with Dagger Hilt.",
            categories = listOf(VideoCategory.ANDROID, VideoCategory.ARCHITECTURE),
            imageUrl = "https://i3.ytimg.com/vi/g-2fcfd4gVE/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.2f,
            url = "https://www.youtube.com/watch?v=g-2fcfd4gVE"
        ),
        Video(
            id = "4",
            title = "What's new in Android 14",
            subtitle = "Android",
            description = "Exploring the latest features in Android 14.",
            categories = listOf(VideoCategory.ANDROID),
            imageUrl = "https://i3.ytimg.com/vi/re2D52tNB_o/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.6f,
            url = "https://www.youtube.com/watch?v=re2D52tNB_o"
        ),
        Video(
            id = "5",
            title = "Introduction to Testing in Android",
            subtitle = "Android",
            description = "A beginner's guide to unit and UI testing in Android.",
            categories = listOf(VideoCategory.ANDROID, VideoCategory.TESTING),
            imageUrl = "https://i3.ytimg.com/vi/EkfEEtJzMNA/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.0f,
            url = "https://www.youtube.com/watch?v=EkfEEtJzMNA"
        ),
        Video(
            id = "6",
            title = "Introduction to Kotlin Multiplatform",
            subtitle = "KMP",
            description = "Learn how to build cross-platform applications with KMP.",
            categories = listOf(VideoCategory.KOTLIN, VideoCategory.IOS),
            imageUrl = "https://i3.ytimg.com/vi/X-3H4I4i-4Y/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.9f,
            progress = 10f,
            url = "https://www.youtube.com/watch?v=X-3H4I4i-4Y"
        )
    )

    override fun list(search: String, category: String, order: OrderType): Flow<List<Video>> {
        return flowOf(videos.filter {
            it.title.contains(search, ignoreCase = true)
        })
    }

    override fun detail(id: String): Flow<Video> {
        return flowOf(videos.first { it.id == id })
    }
}
