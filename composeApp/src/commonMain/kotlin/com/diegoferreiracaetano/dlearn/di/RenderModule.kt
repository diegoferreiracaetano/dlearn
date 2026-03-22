package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.factory.AppContainerRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppEmptyStateRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppErrorRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppExpandableSectionRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppFeedbackRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppHtmlTextRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppListRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppLoadingRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppMainContentRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppMovieDetailHeaderRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppProfileHeaderRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSearchBarRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSearchContentRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSectionTitleRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSelectionRowRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSnackbarRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppSwitchRowRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppTextFieldRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppTopBarListRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.AppTopBarRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.BannerCarouselRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.BottomNavigationRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.CarouselRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.ChipGroupRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.FooterRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.FullScreenBannerRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.MovieCarouselRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.MovieItemRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.PremiumBannerRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.ProfileRowRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.factory.SectionRenderer
import com.diegoferreiracaetano.dlearn.ui.factory.UserRowRenderer
import org.koin.dsl.module

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
