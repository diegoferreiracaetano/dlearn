package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

object RenderComponentFactory {

    private val appLoadingRenderer = AppLoadingRenderer()
    private val appErrorRenderer = AppErrorRenderer()
    private val appEmptyStateRenderer = AppEmptyStateRenderer()
    private val appFeedbackRenderer = AppFeedbackRenderer()
    private val appContainerRenderer = AppContainerRenderer()
    private val appListRenderer = AppListRenderer()
    private val appTopBarRenderer = AppTopBarRenderer()
    private val appSearchBarRenderer = AppSearchBarRenderer()
    private val bottomNavigationRenderer = BottomNavigationRenderer()
    private val movieCarouselRenderer = MovieCarouselRenderer()
    private val bannerCarouselRenderer = BannerCarouselRenderer()
    private val carouselRenderer = CarouselRenderer()
    private val fullScreenBannerRenderer = FullScreenBannerRenderer()
    private val chipGroupRenderer = ChipGroupRenderer()
    private val profileRowRenderer = ProfileRowRenderer()
    private val userRowRenderer = UserRowRenderer()
    private val premiumBannerRenderer = PremiumBannerRenderer()
    private val sectionRenderer = SectionRenderer()
    private val footerRenderer = FooterRenderer()
    private val appMovieDetailHeaderRenderer = AppMovieDetailHeaderRenderer()
    private val appExpandableSectionRenderer = AppExpandableSectionRenderer()
    private val appMainContentRenderer = AppMainContentRenderer()
    private val appSearchContentRenderer = AppSearchContentRenderer()

    @Composable
    fun Render(
        components: List<Component>,
        actions: ComponentActions,
        modifier: Modifier = Modifier
    ) {
        components.forEach {
            Render(it, actions, modifier)
        }
    }

    @Composable
    fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier = Modifier
    ) {
        when (component) {
            is AppLoadingComponent -> appLoadingRenderer.Render(component, actions, modifier)
            is AppErrorComponent -> appErrorRenderer.Render(component, actions, modifier)
            is AppEmptyStateComponent -> appEmptyStateRenderer.Render(component, actions, modifier)
            is AppFeedbackComponent -> appFeedbackRenderer.Render(component, actions, modifier)
            is AppContainerComponent -> appContainerRenderer.Render(component, actions, modifier)
            is AppListComponent -> appListRenderer.Render(component, actions, modifier)
            is AppTopBarComponent -> appTopBarRenderer.Render(component, actions, modifier)
            is AppSearchBarComponent -> appSearchBarRenderer.Render(component, actions, modifier)
            is BottomNavigationComponent -> bottomNavigationRenderer.Render(component, actions, modifier)
            is MovieCarouselComponent -> movieCarouselRenderer.Render(component, actions, modifier)
            is BannerCarouselComponent -> bannerCarouselRenderer.Render(component, actions, modifier)
            is CarouselComponent -> carouselRenderer.Render(component, actions, modifier)
            is FullScreenBannerComponent -> fullScreenBannerRenderer.Render(component, actions, modifier)
            is ChipGroupComponent -> chipGroupRenderer.Render(component, actions, modifier)
            is ProfileRowComponent -> profileRowRenderer.Render(component, actions, modifier)
            is UserRowComponent -> userRowRenderer.Render(component, actions, modifier)
            is PremiumBannerComponent -> premiumBannerRenderer.Render(component, actions, modifier)
            is SectionComponent -> sectionRenderer.Render(component, actions, modifier)
            is FooterComponent -> footerRenderer.Render(component, actions, modifier)
            is AppMovieDetailHeaderComponent -> appMovieDetailHeaderRenderer.Render(component, actions, modifier)
            is AppExpandableSectionComponent -> appExpandableSectionRenderer.Render(component, actions, modifier)
            is AppMainContentComponent -> appMainContentRenderer.Render(component, actions, modifier)
            is AppSearchContentComponent -> appSearchContentRenderer.Render(component, actions, modifier)
            else -> {} // Unknown components
        }
    }
}
