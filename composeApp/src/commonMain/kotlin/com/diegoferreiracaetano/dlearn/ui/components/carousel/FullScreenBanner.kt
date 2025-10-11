package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.diegoferreiracaetano.dlearn.ui.components.button.AppButton
import com.diegoferreiracaetano.dlearn.ui.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.action_add_to_list
import dlearn.composeapp.generated.resources.action_watch
import dlearn.composeapp.generated.resources.banner1
import dlearn.composeapp.generated.resources.banner2
import dlearn.composeapp.generated.resources.banner3
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


private const val RATIO = 12f / 16f

@Composable
private fun FullScreenVideo(
    item: Video,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onWatchClick: () -> Unit,
    onAddToListClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(RATIO)
            .clickable(onClick = onItemClick),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            AppImage(
                imageURL = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Unspecified.copy(alpha = 0.85f)
                            ),
                            startY = 500f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    AppButton(
                        modifier = Modifier.weight(1f),
                        onClick = onWatchClick,
                        text = stringResource(Res.string.action_watch)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    AppButton(
                        modifier = Modifier.weight(1f),
                        onClick = onAddToListClick,
                        text = stringResource(Res.string.action_add_to_list),
                        type = ButtonType.SECONDARY
                    )
                }

                Spacer(modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}

@Composable
fun FullScreenBanner(
    modifier: Modifier = Modifier,
    banners: List<Video>,
    onItemClick: (Video) -> Unit,
    onWatchClick: (Video) -> Unit,
    onAddToListClick: (Video) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState
        ) { pageIndex ->
            val banner = banners[pageIndex]
            FullScreenVideo(
                item = banner,
                onItemClick = { onItemClick(banner) },
                onWatchClick = { onWatchClick(banner) },
                onAddToListClick = { onAddToListClick(banner) },
            )
        }

        if (banners.size > 1) {
            PageIndicator(
                banners.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(BottomCenter)
            )
        }
    }
}

@Preview
@Composable
fun FullScreenVideoPreview() {

    val itemVideo = Video(
        id = "1",
        title = "Introduction to Jetpack Compose",
        subtitle = "Jetpack Compose",
        description = "A comprehensive guide to Jetpack Compose for beginners.",
        categories = listOf(VideoCategory.JETPACK_COMPOSE, VideoCategory.ANDROID),
        imageUrl = "https://i3.ytimg.com/vi/n2t5_qA1Q-o/maxresdefault.jpg",
        isFavorite = false,
        rating = 4.5f,
        url = "https://www.youtube.com/watch?v=n2t5_qA1Q-o"
    )

    DLearnTheme {
        FullScreenVideo(
            item = itemVideo,
            onItemClick = { println("Clicked") },
            onWatchClick = { println("Watch Clicked") },
            onAddToListClick = { println("Add to List Clicked") }
        )
    }

}

@Preview
@Composable
fun FullScreenBannerPreview() {
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
        )
    )

    DLearnTheme {
        FullScreenBanner(
            banners = dummyBanners,
            onItemClick = { item -> println("Clicked ${item.title}") },
            onWatchClick = { item -> println("Watch ${item.title}") },
            onAddToListClick = { item -> println("Add to List ${item.title}") }
        )
    }
}

