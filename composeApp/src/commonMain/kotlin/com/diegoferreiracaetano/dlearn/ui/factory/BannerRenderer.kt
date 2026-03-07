package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCard
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class BannerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val banner = component as? BannerComponent ?: return
        BannerCard(
            title = banner.title,
            subtitle = banner.subtitle.orEmpty(),
            imageSource = AppImageSource.Url(banner.imageUrl),
            onClick = { banner.actionUrl?.let { actions.onItemClick(it) } },
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
