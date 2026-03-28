package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeDataService(
    private val tmdbClient: TmdbClient
) {
    suspend fun fetchHomeData(
        language: String,
        type: HomeFilterType = HomeFilterType.ALL,
        header: AppHeader? = null
    ): HomeDomainData = coroutineScope {
        val sessionId = header?.tmdbSessionId
        val accountId = header?.tmdbAccountId

        val favoritesDeferred = async {
            if (sessionId != null && accountId != null) {
                tmdbClient.getFavoriteMovies(accountId, sessionId, language).results.map { it.id }
            } else emptyList<Int>()
        }

        val watchlistDeferred = async {
            if (sessionId != null && accountId != null) {
                tmdbClient.getWatchlistMovies(accountId, sessionId, language).results.map { it.id }
            } else emptyList<Int>()
        }

        val movieGenres = async { tmdbClient.getMovieGenres(language).genres }
        val tvGenres = async { tmdbClient.getTvGenres(language).genres }

        val favorites = favoritesDeferred.await()
        val watchlist = watchlistDeferred.await()
        val mGenres = movieGenres.await()
        val tGenres = tvGenres.await()

        val genres = if (type == HomeFilterType.SERIES) tGenres else mGenres

        val popularMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getPopularMovies(language).results.map { 
                it.toVideo(MediaType.MOVIE, mGenres, favorites.contains(it.id)) 
            }
        }

        val popularSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList<Video>()
            else tmdbClient.getPopularSeries(language).results.map { 
                it.toVideo(MediaType.SERIES, tGenres, favorites.contains(it.id)) 
            }
        }

        val topRatedMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getTopRatedMovies(language).results.map { 
                it.toVideo(MediaType.MOVIE, mGenres, favorites.contains(it.id)) 
            }
        }

        val topRatedSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList<Video>()
            else tmdbClient.getTopRatedSeries(language).results.map { 
                it.toVideo(MediaType.SERIES, tGenres, favorites.contains(it.id)) 
            }
        }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val topRatedSeries = topRatedSeriesDeferred.await()

        val categoryVideosMap = genres.take(4).associate { category ->
            category.name to async {
                if (type == HomeFilterType.SERIES) {
                    tmdbClient.getTvByGenre(category.id, language).results.map { 
                        it.toVideo(MediaType.SERIES, tGenres, favorites.contains(it.id)) 
                    }
                } else {
                    tmdbClient.getMoviesByGenre(category.id, language).results.map { 
                        it.toVideo(MediaType.MOVIE, mGenres, favorites.contains(it.id)) 
                    }
                }
            }
        }.mapValues { it.value.await() }

        HomeDomainData(
            banner = popularMovies.firstOrNull() ?: popularSeries.firstOrNull(),
            top10 = (topRatedMovies + topRatedSeries).sortedByDescending { it.rating ?: 0f }.take(10),
            popular = (popularMovies + popularSeries).shuffled().take(10),
            categories = categoryVideosMap
        )
    }
}
