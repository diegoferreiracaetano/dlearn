package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppExpandableSectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppFeedbackComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppHtmlTextComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMainContentComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppProfileHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.koin.compose.koinInject

@Composable
fun RenderComponent(
    component: Component,
    actions: ComponentActions,
    modifier: Modifier = Modifier,
) {
    val factory: RenderComponentFactory = koinInject()
    factory.Render(component, actions, modifier)
}

@Composable
fun RenderComponents(
    components: List<Component>,
    actions: ComponentActions,
    modifier: Modifier = Modifier,
) {
    val factory: RenderComponentFactory = koinInject()
    factory.Render(components, actions, modifier)
}

@Suppress("LongParameterList")
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
    private val appSectionTitleRenderer: AppSectionTitleRenderer,
    private val appProfileHeaderRenderer: AppProfileHeaderRenderer,
    private val appTextFieldRenderer: AppTextFieldRenderer,
    private val appSnackbarRenderer: AppSnackbarRenderer,
    private val appHtmlTextRenderer: AppHtmlTextRenderer,
    private val appSwitchRowRenderer: AppSwitchRowRenderer,
    private val appSelectionRowRenderer: AppSelectionRowRenderer,
) {
    @Composable
    fun Render(
        components: List<Component>,
        actions: ComponentActions,
        modifier: Modifier = Modifier,
    ) {
        components.forEach {
            Render(it, actions, modifier)
        }
    }

    @Composable
    fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier = Modifier,
    ) {
        if (!renderBasicComponent(component, actions, modifier)) {
            renderMediaComponent(component, actions, modifier)
        }
    }

    @Composable
    @Suppress("CyclomaticComplexMethod")
    private fun renderBasicComponent(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ): Boolean {
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
            is BottomNavigationComponent -> bottomNavigationRenderer.Render(
                component,
                actions,
                modifier
            )

            is AppSectionTitleComponent -> appSectionTitleRenderer.Render(
                component,
                actions,
                modifier
            )

            is AppProfileHeaderComponent -> appProfileHeaderRenderer.Render(
                component,
                actions,
                modifier
            )

            is AppTextFieldComponent -> appTextFieldRenderer.Render(component, actions, modifier)
            is AppSnackbarComponent -> appSnackbarRenderer.Render(component, actions, modifier)
            is AppHtmlTextComponent -> appHtmlTextRenderer.Render(component, actions, modifier)
            is AppSwitchRowComponent -> appSwitchRowRenderer.Render(component, actions, modifier)
            is AppSelectionRowComponent -> appSelectionRowRenderer.Render(
                component,
                actions,
                modifier
            )

            else -> return false
        }
        return true
    }

    @Composable
    @Suppress("CyclomaticComplexMethod")
    private fun renderMediaComponent(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        when (component) {
            is MovieCarouselComponent -> movieCarouselRenderer.Render(component, actions, modifier)
            is BannerCarouselComponent -> bannerCarouselRenderer.Render(
                component,
                actions,
                modifier
            )

            is CarouselComponent -> carouselRenderer.Render(component, actions, modifier)
            is FullScreenBannerComponent -> fullScreenBannerRenderer.Render(
                component,
                actions,
                modifier
            )

            is ChipGroupComponent -> chipGroupRenderer.Render(component, actions, modifier)
            is ProfileRowComponent -> profileRenderer.Render(component, actions, modifier)
            is UserRowComponent -> userRenderer.Render(component, actions, modifier)
            is PremiumBannerComponent -> premiumBannerRenderer.Render(component, actions, modifier)
            is SectionComponent -> sectionRenderer.Render(component, actions, modifier)
            is FooterComponent -> footerRenderer.Render(component, actions, modifier)
            is AppMovieDetailHeaderComponent -> movieDetailHeaderRenderer.Render(
                component,
                actions,
                modifier
            )

            is AppExpandableSectionComponent -> expandableSectionRenderer.Render(
                component,
                modifier
            )

            is AppMainContentComponent -> mainContentRenderer.Render(component, actions, modifier)
            is MovieItemComponent -> movieItemRenderer.Render(component, actions, modifier)
            else -> Unit
        }
    }
}
