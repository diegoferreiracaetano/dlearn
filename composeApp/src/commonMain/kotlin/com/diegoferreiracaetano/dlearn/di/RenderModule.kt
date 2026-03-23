package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.factory.*
import org.koin.dsl.module

val renderModule = module {
    single { AppLoadingRenderer() }
    single { AppErrorRenderer() }
    single { AppEmptyStateRenderer() }
    single { AppFeedbackRenderer() }
    single { AppContainerRenderer() }
    single { AppListRenderer() }
    single { AppTopBarRenderer() }
    single { AppTopBarListRenderer() }
    single { AppSearchBarRenderer() }
    single { BottomNavigationRenderer() }
    single { MovieCarouselRenderer() }
    single { BannerCarouselRenderer() }
    single { CarouselRenderer() }
    single { FullScreenBannerRenderer() }
    single { ChipGroupRenderer() }
    single { ProfileRowRenderer() }
    single { UserRowRenderer() }
    single { PremiumBannerRenderer() }
    single { SectionRenderer() }
    single { FooterRenderer() }
    single { AppMovieDetailHeaderRenderer() }
    single { AppExpandableSectionRenderer() }
    single { AppMainContentRenderer() }
    single { MovieItemRenderer() }
    single { AppSearchContentRenderer() }
    single { AppSectionTitleRenderer() }
    single { AppProfileHeaderRenderer() }
    single { AppTextFieldRenderer() }
    single { AppSnackbarRenderer() }
    single { AppHtmlTextRenderer() }
    single { AppSwitchRowRenderer() }
    single { AppSelectionRowRenderer() }

    single {
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
