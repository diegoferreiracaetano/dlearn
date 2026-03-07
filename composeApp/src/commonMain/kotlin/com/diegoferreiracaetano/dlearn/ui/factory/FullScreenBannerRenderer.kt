package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.FullScreenVideo
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class FullScreenBannerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val banner = component as? FullScreenBannerComponent ?: return
        FullScreenVideo(
            title = banner.title,
            subtitle = banner.subtitle.orEmpty(),
            imageSource = AppImageSource.Url(banner.imageUrl),
            onItemClick = { actions.onItemClick(banner.id) },
            onWatchClick = { actions.onItemClick(banner.id) },
            onAddToListClick = { /* Handle add to list */ }
        )
    }
}
