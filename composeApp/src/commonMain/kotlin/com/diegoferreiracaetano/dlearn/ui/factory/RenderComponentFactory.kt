package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

/**
 * CompositionLocal to provide the [RenderComponentFactory] throughout the UI hierarchy.
 */
val LocalRenderComponentFactory = staticCompositionLocalOf<RenderComponentFactory> {
    error("No RenderComponentFactory provided")
}

/**
 * Top-level Composable function to render a SDUI component using the provided factory.
 */
@Composable
fun RenderComponent(
    component: Component,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    LocalRenderComponentFactory.current.Render(component, actions, modifier)
}

/**
 * Top-level Composable function to render a list of SDUI components.
 */
@Composable
fun RenderComponents(
    components: List<Component>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    LocalRenderComponentFactory.current.Render(components, actions, modifier)
}

/**
 * Factory responsible for rendering UI components based on their type.
 * Uses Dependency Injection to provide specific renderers for each component.
 */
class RenderComponentFactory(
    private val appLoadingRenderer: AppLoadingRenderer,
    private val appErrorRenderer: AppErrorRenderer,
    private val appEmptyStateRenderer: AppEmptyStateRenderer,
    private val appFeedbackRenderer: AppFeedbackRenderer,
    private val appContainerRenderer: AppContainerRenderer,
    private val appListRenderer: AppListRenderer,
    private val appTopBarRenderer: AppTopBarRenderer,
    private val appTopBarListRenderer: AppTopBarListRenderer,
    private val appSearchBarRenderer: AppSearchBarRenderer,
    private val bottomNavigationRenderer: BottomNavigationRenderer,
    private val movieCarouselRenderer: MovieCarouselRenderer,
    private val bannerCarouselRenderer: BannerCarouselRenderer,
    private val carouselRenderer: CarouselRenderer,
    private val fullScreenBannerRenderer: FullScreenBannerRenderer,
    private val chipGroupRenderer: ChipGroupRenderer,
    private val profileRenderer: ProfileRowRenderer,
    private val userRenderer: UserRowRenderer,
    private val premiumBannerRenderer: PremiumBannerRenderer,
    private val sectionRenderer: SectionRenderer,
    private val footerRenderer: FooterRenderer,
    private val movieDetailHeaderRenderer: AppMovieDetailHeaderRenderer,
    private val expandableSectionRenderer: AppExpandableSectionRenderer,
    private val mainContentRenderer: AppMainContentRenderer,
    private val movieItemRenderer: MovieItemRenderer,
    private val searchContentRenderer: AppSearchContentRenderer,
    private val appSectionTitleRenderer: AppSectionTitleRenderer,
    private val appProfileHeaderRenderer: AppProfileHeaderRenderer,
    private val appTextFieldRenderer: AppTextFieldRenderer,
    private val appSnackbarRenderer: AppSnackbarRenderer,
    private val appHtmlTextRenderer: AppHtmlTextRenderer,
) {

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
            is AppTopBarListComponent -> appTopBarListRenderer.Render(component, actions, modifier)
            is AppSearchBarComponent -> appSearchBarRenderer.Render(component, actions, modifier)
            is BottomNavigationComponent -> bottomNavigationRenderer.Render(component, actions, modifier)
            is MovieCarouselComponent -> movieCarouselRenderer.Render(component, actions, modifier)
            is BannerCarouselComponent -> bannerCarouselRenderer.Render(component, actions, modifier)
            is CarouselComponent -> carouselRenderer.Render(component, actions, modifier)
            is FullScreenBannerComponent -> fullScreenBannerRenderer.Render(component, actions, modifier)
            is ChipGroupComponent -> chipGroupRenderer.Render(component, actions, modifier)
            is ProfileRowComponent -> profileRenderer.Render(component, actions, modifier)
            is UserRowComponent -> userRenderer.Render(component, actions, modifier)
            is PremiumBannerComponent -> premiumBannerRenderer.Render(component, actions, modifier)
            is SectionComponent -> sectionRenderer.Render(component, actions, modifier)
            is FooterComponent -> footerRenderer.Render(component, actions, modifier)
            is AppMovieDetailHeaderComponent -> movieDetailHeaderRenderer.Render(component, actions, modifier)
            is AppExpandableSectionComponent -> expandableSectionRenderer.Render(component, actions, modifier)
            is AppMainContentComponent -> mainContentRenderer.Render(component, actions, modifier)
            is MovieItemComponent -> movieItemRenderer.Render(component, actions, modifier)
            is AppSearchContentComponent -> searchContentRenderer.Render(component, actions, modifier)
            is AppSectionTitleComponent -> appSectionTitleRenderer.Render(component, actions, modifier)
            is AppProfileHeaderComponent -> appProfileHeaderRenderer.Render(component, actions, modifier)
            is AppTextFieldComponent -> appTextFieldRenderer.Render(component, actions, modifier)
            is AppSnackbarComponent -> appSnackbarRenderer.Render(component, actions, modifier)
            is AppHtmlTextComponent -> appHtmlTextRenderer.Render(component, actions, modifier)
        }
    }
}
