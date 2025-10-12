package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Carousel(
    title: String,
    items: List<Video>,
    modifier: Modifier = Modifier,
    onItemClick: (Video) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
            ),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { _, item ->
                Box(modifier = Modifier.clickable { onItemClick(item) }) {
                    MovieVideoCard(item)
                }
            }
        }
    }
}

@Composable
fun MovieVideoCard(
    item: Video,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(140.dp),
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
        ) {
            AppImage(
                imageURL = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
            )
            if (item.rating > 0) {
                RatingBadge(
                    rating = item.rating.toString(),
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = item.subtitle,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RatingBadge(
    rating: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = rating,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselPreview() {
    val items =listOf(
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
        Carousel(
            title = "New Releases",
            items = items,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieVideoCardPreview() {
    val item = Video(
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
        MovieVideoCard(item = item)
    }
}