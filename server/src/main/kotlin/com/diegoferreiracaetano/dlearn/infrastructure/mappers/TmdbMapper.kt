package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.models.CastMemberDomainData
import com.diegoferreiracaetano.dlearn.domain.models.EpisodeDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.models.SeasonDomainData
import com.diegoferreiracaetano.dlearn.domain.models.WatchProviderDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.TmdbCastRemote
import com.diegoferreiracaetano.dlearn.model.TmdbEpisodeRemote
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import com.diegoferreiracaetano.dlearn.model.TmdbSeasonRemote
import com.diegoferreiracaetano.dlearn.model.WatchProviderRemote
import java.util.Locale

class TmdbMapper(
    private val urlMapper: WatchProviderUrlMapper,
) {
    fun toMovieDetail(
        response: TmdbMovieDetailRemote,
        isFavorite: Boolean = false,
        isInWatchlist: Boolean = false,
        episodes: List<EpisodeDomainData> = emptyList(),
    ): MovieDetailDomainData {
        val movieTitle = response.title ?: response.name.orEmpty()
        val countryProviders = response.watchProviders?.results?.get(TmdbConstants.DEFAULT_REGION)
        val imdbId = response.externalIds?.imdbId

        val mediaType = if (response.title != null) MediaType.MOVIES else MediaType.SERIES

        return MovieDetailDomainData(
            id = "${mediaType.name}_${response.id}",
            title = movieTitle,
            imageUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}${response.posterPath}",
            year = (
                response.releaseDate
                    ?: response.firstAirDate.orEmpty()
                ).take(TmdbConstants.YEAR_CHAR_COUNT),
            duration = response.runtime?.toString().orEmpty(),
            genre =
            response.genres
                .firstOrNull()
                ?.name
                .orEmpty(),
            rating = String.format(Locale.US, "%.1f", response.voteAverage ?: 0.0),
            storyLine = response.overview.orEmpty(),
            cast =
            response.credits
                ?.cast
                ?.take(TmdbConstants.MAX_CAST_SIZE)
                ?.map { toCastMember(it) }
                .orEmpty(),
            seasons = response.seasons
                .filter { it.seasonNumber > 0 }
                .map { toSeason(it) },
            episodes = episodes,
            trailerId = getTrailerId(response),
            isFavorite = isFavorite,
            isInWatchlist = isInWatchlist,
            providers =
            countryProviders
                ?.flatrate
                ?.map { provider ->
                    toWatchProvider(provider, movieTitle, imdbId, countryProviders.link)
                }.orEmpty(),
            mediaType = mediaType,
        )
    }

    private fun toSeason(season: TmdbSeasonRemote) =
        SeasonDomainData(
            number = season.seasonNumber,
            episodeCount = season.episodeCount,
        )

    fun toEpisode(episode: TmdbEpisodeRemote) =
        EpisodeDomainData(
            id = episode.id,
            name = episode.name,
            overview = episode.overview,
            episodeNumber = episode.episodeNumber,
            seasonNumber = episode.seasonNumber,
            imageUrl = episode.stillPath?.let { "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W300}$it" },
            duration = episode.runtime?.toString(),
        )

    private fun getTrailerId(response: TmdbMovieDetailRemote): String? =
        response.videos
            ?.results
            ?.filter {
                it.site == TmdbConstants.SITE_YOUTUBE &&
                    (it.type == TmdbConstants.TYPE_TRAILER || it.type == TmdbConstants.TYPE_TEASER)
            }?.firstOrNull()
            ?.key

    private fun toCastMember(cast: TmdbCastRemote) =
        CastMemberDomainData(
            name = cast.name,
            role = cast.character,
            imageUrl =
            cast.profilePath?.let { path ->
                "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W185}$path"
            },
        )

    private fun toWatchProvider(
        provider: WatchProviderRemote,
        title: String,
        imdbId: String?,
        fallbackUrl: String?,
    ): WatchProviderDomainData {
        val urls = urlMapper.buildUrls(provider.providerId, title, imdbId, fallbackUrl)
        return WatchProviderDomainData(
            id = provider.providerId,
            name = provider.providerName.orEmpty(),
            iconUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W185}${provider.logoPath}",
            priceInfo = "",
            appUrl = urls.appUrl,
            webUrl = urls.webUrl,
            tmdbUrl = urls.tmdbUrl,
        )
    }
}
