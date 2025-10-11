package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContinueWatchingCarousel(
    title: String,
    items: List<Video>,
    onItemClick: (Video) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 8.dp,
            ),
        )

       LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items.size) { index ->
                val item = items[index]
                ContinueWatchingCard(
                    item = item,
                    modifier = Modifier.width(350.dp), // ← largura maior para cortar o 2º
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

private const val RATIO = 16f / 9f

@Composable
fun ContinueWatchingCard(
    item: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(RATIO)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AppImage(
                imageURL = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente e ícone de play centralizado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Unspecified.copy(alpha = 0.8f)
                            ),
                        )
                    )
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ContinueWatchingPreview() {
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
        ContinueWatchingCarousel(
            title = "Continue Watching",
            items = dummyBanners,
            onItemClick = {}
        )
    }
}
