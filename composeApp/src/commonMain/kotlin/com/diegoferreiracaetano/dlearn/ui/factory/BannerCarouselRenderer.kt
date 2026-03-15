package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.AppBannerCarousel
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCard
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toMovieItem

class BannerCarouselRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val carousel = component as? BannerCarouselComponent ?: return
        val items = carousel.items.map { it.toMovieItem() }
        AppBannerCarousel(
            modifier = modifier,
            title = carousel.title,
            itemCount = items.size,
            itemContent = { index ->
                val item = items[index]
                BannerCard(
                    title = item.title,
                    subtitle = item.genre,
                    imageSource = item.imageSource,
                    onClick = { actions.onItemClick(item.id) }
                )
            }
        )
    }
}
