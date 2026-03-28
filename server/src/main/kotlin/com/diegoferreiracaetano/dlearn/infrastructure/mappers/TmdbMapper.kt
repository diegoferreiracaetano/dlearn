package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.models.CastMemberDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.models.WatchProviderDomainData
import com.diegoferreiracaetano.dlearn.model.*
import java.util.Locale

class TmdbMapper(private val urlMapper: WatchProviderUrlMapper) {

    fun toMovieDetail(
        response: TmdbMovieDetailRemote,
        accountStates: TmdbAccountStatesRemote? = null
    ): MovieDetailDomainData {
        val movieTitle = response.title ?: response.name ?: ""
        val countryProviders = response.watchProviders?.results?.get(TmdbConstants.DEFAULT_REGION)
        val imdbId = response.externalIds?.imdbId

        return MovieDetailDomainData(
            id = response.id.toString(),
            title = movieTitle,
            imageUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}${response.posterPath}",
            year = (response.releaseDate ?: response.firstAirDate ?: "").take(4),
            duration = response.runtime?.toString() ?: "",
            genre = response.genres.firstOrNull()?.name ?: "",
            rating = String.format(Locale.US, "%.1f", response.voteAverage ?: 0.0),
            storyLine = response.overview ?: "",
            cast = response.credits?.cast?.take(10)?.map { toCastMember(it) } ?: emptyList(),
            seasons = emptyList(),
            trailerId = response.videos?.results
                ?.filter { it.site == TmdbConstants.SITE_YOUTUBE && (it.type == TmdbConstants.TYPE_TRAILER || it.type == TmdbConstants.TYPE_TEASER) }
                ?.firstOrNull()?.key,
            isFavorite = accountStates?.favorite ?: false,
            isInList = accountStates?.watchlist ?: false,
            providers = countryProviders?.flatrate?.map { provider ->
                toWatchProvider(provider, movieTitle, imdbId, countryProviders.link)
            } ?: emptyList()
        )
    }

    private fun toCastMember(cast: TmdbCastRemote) = CastMemberDomainData(
        name = cast.name,
        role = cast.character,
        imageUrl = cast.profilePath?.let { path -> "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W185}$path" }
    )

    private fun toWatchProvider(
        provider: WatchProviderRemote,
        title: String,
        imdbId: String?,
        fallbackUrl: String?
    ): WatchProviderDomainData {
        val urls = urlMapper.buildUrls(provider.providerId, title, imdbId, fallbackUrl)
        return WatchProviderDomainData(
            id = provider.providerId,
            name = provider.providerName ?: "",
            iconUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W185}${provider.logoPath}",
            priceInfo = "",
            appUrl = urls.appUrl,
            webUrl = urls.webUrl,
            tmdbUrl = urls.tmdbUrl
        )
    }
}
