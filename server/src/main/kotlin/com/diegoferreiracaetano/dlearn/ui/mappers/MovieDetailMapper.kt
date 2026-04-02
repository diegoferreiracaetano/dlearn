package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WATCHLIST
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppExpandableSectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.WatchProviderComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MovieDetailMapper(
    private val i18n: I18nProvider,
) {
    fun toHeader(
        data: MovieDetailDomainData,
        lang: String,
    ): AppMovieDetailHeaderComponent =
        AppMovieDetailHeaderComponent(
            title = data.title,
            imageUrl = data.imageUrl,
            year = data.year,
            duration = data.duration,
            genre = data.genre,
            rating = data.rating.toDoubleOrNull(),
            trailerId = data.trailerId,
            isFavorite = data.isFavorite,
            isInWatchlist = data.isInWatchlist,
            favoriteActionUrl =
                AppPath(
                    path = AppNavigationRoute.MOVIES,
                    params =
                        mapOf(
                            AppQueryParam.ID to data.id,
                            AppQueryParam.MEDIA_TYPE to data.mediaType.name,
                            FAVORITE to (!data.isFavorite).toString(),
                        ),
                ),
            watchlistActionUrl =
                AppPath(
                    path = AppNavigationRoute.MOVIES,
                    params =
                        mapOf(
                            AppQueryParam.ID to data.id,
                            AppQueryParam.MEDIA_TYPE to data.mediaType.name,
                            WATCHLIST to (!data.isInWatchlist).toString(),
                        ),
                ),
            providers =
                data.providers.map {
                    WatchProviderComponent(
                        name = it.name,
                        iconUrl = it.iconUrl,
                        priceInfo = i18n.getString(AppStringType.DETAIL_WATCH_NOW, lang),
                        appUrl = it.appUrl,
                        webUrl = it.webUrl,
                        tmdbUrl = it.tmdbUrl,
                    )
                },
        )

    fun toStoryLine(
        data: MovieDetailDomainData,
        lang: String,
    ): AppExpandableSectionComponent =
        AppExpandableSectionComponent(
            title = i18n.getString(AppStringType.DETAIL_STORY_LINE, lang),
            text = data.storyLine,
        )

    fun toCastCarousel(
        data: MovieDetailDomainData,
        lang: String,
    ): CarouselComponent =
        CarouselComponent(
            title = i18n.getString(AppStringType.DETAIL_CAST_CREW, lang),
            items =
                data.cast.map {
                    UserRowComponent(
                        name = it.name,
                        role = it.role,
                        imageUrl = it.imageUrl ?: "",
                    )
                },
        )

    fun toEpisodesSection(
        data: MovieDetailDomainData,
        lang: String,
    ): SectionComponent? {
        if (data.seasons.isEmpty()) return null
        return SectionComponent(
            title = i18n.getString(AppStringType.DETAIL_EPISODE, lang),
            items = emptyList(),
        )
    }
}
