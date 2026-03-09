package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.AppCarousel
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class CarouselRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val carousel = (component as? CarouselComponent) ?: return
        AppCarousel(
            itemCount = carousel.items.size,
            title = carousel.title,
            isPager = false,
            spacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier,
        ) { index ->
            RenderComponentFactory.Render(
                component = carousel.items[index],
                actions = actions,
            )
        }
    }
}
