package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

object RenderComponentFactory {
    @Composable
    fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier = Modifier.Companion
    ) {
        when (component) {
            is AppContainerComponent -> AppContainerRenderer().Render(component, actions, modifier)
            is AppTopBarComponent -> AppTopBarRenderer().Render(component, actions, modifier)
            is BottomNavigationComponent -> BottomNavigationRenderer().Render(component, actions, modifier)
            is MovieCarouselComponent -> MovieCarouselRenderer().Render(component, actions, modifier)
            is BannerCarouselComponent -> BannerCarouselRenderer().Render(component, actions, modifier)
            is BannerComponent -> BannerRenderer().Render(component, actions, modifier)
            is FullScreenBannerComponent -> FullScreenBannerRenderer().Render(component, actions, modifier)
            is ChipGroupComponent -> ChipGroupRenderer().Render(component, actions, modifier)
            else -> {} // Unknown components
        }
    }
}