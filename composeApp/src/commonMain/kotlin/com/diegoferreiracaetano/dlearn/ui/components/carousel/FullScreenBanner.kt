package com.diegoferreiracaetano.dlearn.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import dlearn.composeapp.generated.resources.banner1
import dlearn.composeapp.generated.resources.banner2
import dlearn.composeapp.generated.resources.banner3
import org.jetbrains.compose.ui.tooling.preview.Preview


private const val RATIO = 12f / 16f

@Composable
private fun FullScreenBannerItem(
    item: BannerItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(RATIO)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        AppImage(
            imageURL = item.imageUrl,
            imageResource = item.imageResource,
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
                            MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.85f)
                        ),
                        startY = 500f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
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
        }
    }
}

@Composable
fun FullScreenBanner(
    banners: List<BannerItem>,
    onItemClick: (BannerItem) -> Unit,
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
            FullScreenBannerItem(
                item = banner,
                onClick = { onItemClick(banner) },
            )
        }

        PageIndicator(
            banners.size,
            currentPage = pagerState.currentPage,
            // Alinha o indicador na parte inferior central da Box
            modifier = Modifier.align(BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun FullScreenBannerItemPreview() {

    val itemBannerItem =  BannerItem(
        id = "1",
        title = "Black Panther: Wakanda Forever",
        subtitle = "On March 2, 2024",
        imageResource = Res.drawable.banner1
    )

    FullScreenBannerItem(itemBannerItem) {
        println("Clicked")
    }

}

@Preview
@Composable
fun FullScreenBannerPreview() {
    val dummyBanners = listOf(
        BannerItem(
            id = "1",
            title = "Black Panther: Wakanda Forever",
            subtitle = "On March 2, 2024",
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
        FullScreenBanner(
            banners = dummyBanners,
            onItemClick = { item -> println("Clicked ${item.title}") },
        )
    }
}

