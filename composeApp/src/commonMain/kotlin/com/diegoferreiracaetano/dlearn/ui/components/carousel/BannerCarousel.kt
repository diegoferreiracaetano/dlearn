package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.banner
import dlearn.composeapp.generated.resources.banner1
import dlearn.composeapp.generated.resources.banner2
import dlearn.composeapp.generated.resources.banner3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.ui.tooling.preview.Preview


data class BannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String? = null,
    val imageResource: DrawableResource? = null
)

@Composable
fun BannerCarousel(
    banners: List<BannerItem>,
    onItemClick: (BannerItem) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
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

private const val RATIO = 16f / 9f

@Composable
private fun BannerCard(
    item: BannerItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .aspectRatio(RATIO)
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AppImage(
                imageResource = item.imageResource,
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
                                MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f)
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
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.subtitle,
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
            item = BannerItem(
                id = "1",
                title = "Black Panther: Wakanda Forever",
                subtitle = "On March 2, 2022",
                imageResource = Res.drawable.banner1
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun BannerCarouselPreview() {
    val dummyBanners = listOf(
        BannerItem(
            id = "1",
            title = "Black Panther: Wakanda Forever",
            subtitle = "On March 2, 2022",
            imageResource = Res.drawable.banner1
        ),
        BannerItem(
            "2",
            "Dune: Part Two",
            "Epic sci-fi adventure",
            imageResource = Res.drawable.banner2
        ),
        BannerItem(
            "3",
            "Spider-Man: No Way Home",
            "The multiverse shattered",
            imageResource = Res.drawable.banner3
        )
    )

    DLearnTheme {
        BannerCarousel(
            banners = dummyBanners,
            onItemClick = { item -> println("Clicked ${item.title}") } // Ação de clique simulada
        )
    }
}
