package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.UIConstants
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
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
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
                    imageUrl = it.imageUrl ?: Constants.EMPTY_STRING,
                )
            },
        )

    fun toEpisodesSection(
        data: MovieDetailDomainData,
        lang: String,
        selectedSeason: Int = 1,
    ): List<Component> {
        if (data.seasons.isEmpty()) return emptyList()

        val seasonLabel = i18n.getString(AppStringType.DETAIL_SEASON, lang)

        val seasonOptions = data.seasons.map { season ->
            ChipItem(
                id = season.number.toString(),
                label = "$seasonLabel ${season.number}",
                isSelected = season.number == selectedSeason,
                actionUrl = AppPath.invoke(
                    path = AppNavigationRoute.MOVIES,
                    params = mapOf(
                        AppQueryParam.ID to data.id,
                        AppQueryParam.SEASON_NUMBER to season.number.toString()
                    )
                )
            )
        }

        return buildList {
            add(
                ChipGroupComponent(
                    items = listOf(
                        ChipItem(
                            id = "season_selector",
                            label = "$seasonLabel $selectedSeason",
                            isSelected = true,
                            hasDropDown = true,
                            isFilter = false,
                            options = seasonOptions,
                            actionUrl = ""
                        )
                    ),
                    cleanUrl = ""
                )
            )
            data.episodes.mapTo(this) { episode ->
                MovieItemComponent(
                    id = "${data.id}_s${episode.seasonNumber}_e${episode.episodeNumber}",
                    title = episode.name,
                    subtitle = episode.overview,
                    imageUrl = episode.imageUrl ?: data.imageUrl,
                    duration = episode.duration?.let { "${it}m" }.orEmpty(),
                    year = episode.airDate,
                    rating = episode.rating,
                    contentRating = UIConstants.DEFAULT_CONTENT_RATING,
                    genre = data.genre,
                    movieType = "Episode",
                    isPremium = false,
                    actionUrl = null
                )
            }
        }
    }
}
