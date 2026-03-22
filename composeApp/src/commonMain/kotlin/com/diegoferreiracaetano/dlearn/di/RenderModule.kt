package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.factory.*
import org.koin.dsl.module

/**
 * Module for UI renderers and the RenderComponentFactory.
 */
val renderModule = module {
    factory { AppLoadingRenderer() }
    factory { AppErrorRenderer() }
    factory { AppEmptyStateRenderer() }
    factory { AppFeedbackRenderer() }
    factory { AppContainerRenderer() }
    factory { AppListRenderer() }
    factory { AppTopBarRenderer() }
    factory { AppTopBarListRenderer() }
    factory { AppSearchBarRenderer() }
    factory { BottomNavigationRenderer() }
    factory { MovieCarouselRenderer() }
    factory { BannerCarouselRenderer() }
    factory { CarouselRenderer() }
    factory { FullScreenBannerRenderer() }
    factory { ChipGroupRenderer() }
    factory { ProfileRowRenderer() }
    factory { UserRowRenderer() }
    factory { PremiumBannerRenderer() }
    factory { SectionRenderer() }
    factory { FooterRenderer() }
    factory { AppMovieDetailHeaderRenderer() }
    factory { AppExpandableSectionRenderer() }
    factory { AppMainContentRenderer() }
    factory { MovieItemRenderer() }
    factory { AppSearchContentRenderer() }
    factory { AppSectionTitleRenderer() }
    factory { AppProfileHeaderRenderer() }
    factory { AppTextFieldRenderer() }
    factory { AppSnackbarRenderer() }
    factory { AppHtmlTextRenderer() }
    factory { AppSwitchRowRenderer() }
    factory { AppSelectionRowRenderer() }

    single<RenderComponentFactory> {
        RenderComponentFactory(
            appLoadingRenderer = get(),
            appErrorRenderer = get(),
            appEmptyStateRenderer = get(),
            appFeedbackRenderer = get(),
            appContainerRenderer = get(),
            appListRenderer = get(),
            appTopBarRenderer = get(),
            appTopBarListRenderer = get(),
            appSearchBarRenderer = get(),
            bottomNavigationRenderer = get(),
            movieCarouselRenderer = get(),
            bannerCarouselRenderer = get(),
            carouselRenderer = get(),
            fullScreenBannerRenderer = get(),
            chipGroupRenderer = get(),
            profileRenderer = get(),
            userRenderer = get(),
            premiumBannerRenderer = get(),
            sectionRenderer = get(),
            footerRenderer = get(),
            movieDetailHeaderRenderer = get(),
            expandableSectionRenderer = get(),
            mainContentRenderer = get(),
            movieItemRenderer = get(),
            searchContentRenderer = get(),
            appSectionTitleRenderer = get(),
            appProfileHeaderRenderer = get(),
            appTextFieldRenderer = get(),
            appSnackbarRenderer = get(),
            appHtmlTextRenderer = get(),
            appSwitchRowRenderer = get(),
            appSelectionRowRenderer = get()
        )
    }
}
