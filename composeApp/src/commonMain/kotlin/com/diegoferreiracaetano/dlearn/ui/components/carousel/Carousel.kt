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
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.cup
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data class CarouselItem(
    val id: Int,
    val imageRes: DrawableResource,
    val title: String,
    val subtitle: String,
    val rating: Float,
)

@Composable
fun Carousel(
    title: String,
    items: List<CarouselItem>,
    modifier: Modifier = Modifier,
    onItemClick: (CarouselItem) -> Unit = {},
    itemContent: @Composable (CarouselItem) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 8.dp,
            ),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { _, item ->
                Box(modifier = Modifier.clickable { onItemClick(item) }) {
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
fun MovieCarouselItemCard(
    item: CarouselItem,
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
                imageResource = item.imageRes,
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
    val items = List(5) { index ->
        CarouselItem(
            id = index,
            imageRes = Res.drawable.cup,
            title = "Movie Title Long Name $index",
            subtitle = "Action, Adventure",
            rating = 4.5f,
        )
    }

    DLearnTheme {
        Carousel(
            title = "New Releases",
            items = items,
            onItemClick = {},
            modifier = Modifier.padding(top = 16.dp),
        ) { item ->
            MovieCarouselItemCard(item = item)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieCarouselItemCardPreview() {
    val item = CarouselItem(
        id = 0,
        imageRes = Res.drawable.cup,
        title = "The Best Movie Ever",
        subtitle = "Comedy",
        rating = 5.0f,
    )
    DLearnTheme {
        MovieCarouselItemCard(item = item)
    }
}