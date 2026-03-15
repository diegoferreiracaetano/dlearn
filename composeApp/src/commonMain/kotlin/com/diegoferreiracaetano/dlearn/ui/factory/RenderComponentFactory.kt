package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

object RenderComponentFactory {

    @Composable
    fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier = Modifier.Companion
    ) {

        when (component) {
            is AppLoadingComponent -> AppLoadingRenderer().Render(component, actions, modifier)
            is AppErrorComponent -> AppErrorRenderer().Render(component, actions, modifier)
            is AppContainerComponent -> AppContainerRenderer().Render(component, actions, modifier)
            is AppListComponent -> AppListRenderer().Render(component, actions, modifier)
            is AppTopBarComponent -> AppTopBarRenderer().Render(component, actions, modifier)
            is SearchBarComponent -> SearchBarRenderer().Render(component, actions, modifier)
            is BottomNavigationComponent -> BottomNavigationRenderer().Render(component, actions, modifier)
            is MovieCarouselComponent -> MovieCarouselRenderer().Render(component, actions, modifier)
            is BannerCarouselComponent -> BannerCarouselRenderer().Render(component, actions, modifier)
            is CarouselComponent -> CarouselRenderer().Render(component, actions, modifier)
            is FullScreenBannerComponent -> FullScreenBannerRenderer().Render(component, actions, modifier)
            is ChipGroupComponent -> ChipGroupRenderer().Render(component, actions, modifier)
            is ProfileRowComponent -> ProfileRowRenderer().Render(component, actions, modifier)
            is UserRowComponent -> UserRowRenderer().Render(component, actions, modifier)
            is PremiumBannerComponent -> PremiumBannerRenderer().Render(component, actions, modifier)
            is SectionComponent -> SectionRenderer().Render(component, actions, modifier)
            is FooterComponent -> FooterRenderer().Render(component, actions, modifier)
            is AppMovieDetailHeaderComponent -> AppMovieDetailHeaderRenderer().Render(component, actions, modifier)
            is AppExpandableSectionComponent -> AppExpandableSectionRenderer().Render(component, actions, modifier)
            is AppMainContentComponent -> AppMainContentRenderer().Render(component, actions, modifier)
            else -> {} // Unknown components
        }
    }
}
