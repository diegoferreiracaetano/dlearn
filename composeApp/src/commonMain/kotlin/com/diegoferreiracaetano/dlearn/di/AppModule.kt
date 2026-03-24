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
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule, sharedModule)
    }
}

val appModule = module {
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
            appSelectionRowRenderer = get(),
        )
    }
}
