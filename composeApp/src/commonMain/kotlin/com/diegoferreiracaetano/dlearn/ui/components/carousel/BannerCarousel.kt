package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.banner1
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BannerCarousel(
    modifier: Modifier = Modifier,
    title: String,
    banners: List<Video>,
    onItemClick: (Video) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp,
                ),
            )

            HorizontalPager(state = pagerState) { pageIndex ->
                val banner = banners[pageIndex]
                BannerCard(
                    item = banner,
                    onClick = { onItemClick(banner) }
                )
            }

            PageIndicator(
                banners.size,
                pagerState.currentPage,
                modifier = Modifier.align(BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }

    }
}

private const val RATIO = 16f / 9f

@Composable
private fun BannerCard(
    item: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .aspectRatio(RATIO)
            .fillMaxSize()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            AppImage(
                imageURL = item.imageUrl,
                contentDescription = item.imageUrl,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Unspecified.copy(alpha = 0.8f)
                            ),
                            startY = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.subtitle,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun BannerCardPreview() {
    DLearnTheme {
        BannerCard(
            item = Video(
                id = "1",
                title = "Introduction to Jetpack Compose",
                subtitle = "Jetpack Compose",
                description = "A comprehensive guide to Jetpack Compose for beginners.",
                categories = listOf(VideoCategory.JETPACK_COMPOSE, VideoCategory.ANDROID),
                imageUrl = "https://i3.ytimg.com/vi/n2t5_qA1Q-o/maxresdefault.jpg",
                isFavorite = false,
                rating = 4.5f,
                url = "https://www.youtube.com/watch?v=n2t5_qA1Q-o"
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun BannerCarouselPreview() {
    val dummyBanners = listOf(
        Video(
            id = "1",
            title = "Introduction to Jetpack Compose",
            subtitle = "Jetpack Compose",
            description = "A comprehensive guide to Jetpack Compose for beginners.",
            categories = listOf(VideoCategory.JETPACK_COMPOSE, VideoCategory.ANDROID),
            imageUrl = "https://i3.ytimg.com/vi/n2t5_qA1Q-o/maxresdefault.jpg",
            isFavorite = false,
            rating = 4.5f,
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
    )

    DLearnTheme {
        BannerCarousel(
            title = "Favoritos",
            banners = dummyBanners,
            onItemClick = { item -> println("Clicked ${item.title}") } // Ação de clique simulada
        )
    }
}
