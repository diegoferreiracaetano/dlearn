package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType.MOVIES
import com.diegoferreiracaetano.dlearn.domain.video.MediaType.SERIES
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants.HomeConfig
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeDataService(
    private val tmdbClient: TmdbClient,
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun fetchHomeData(
        language: String,
        type: HomeFilterType = HomeFilterType.ALL,
        header: AppHeader? = null
    ): HomeDomainData = coroutineScope {
        val userId = header?.userId ?: AppConstants.GUEST_USER_ID

        // 1. Buscamos favoritos do banco local (Source of Truth)
        val favoritesDeferred = async {
            runCatching {
                favoriteRepository.getFavorites(userId).map { it.first }
            }.getOrElse { emptyList() }
        }

        val movieGenres = async { runCatching { tmdbClient.getMovieGenres(language).genres }.getOrElse { emptyList() } }
        val tvGenres = async { runCatching { tmdbClient.getTvGenres(language).genres }.getOrElse { emptyList() } }

        val favorites = favoritesDeferred.await()
        val mGenres = movieGenres.await()
        val tGenres = tvGenres.await()

        val genres = if (type == HomeFilterType.SERIES) tGenres else mGenres

        val popularMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else runCatching { 
                tmdbClient.getPopularMovies(language).results.map { 
                    it.toVideo(MOVIES, mGenres, favorites.contains(it.id))
                }
            }.getOrElse { emptyList() }
        }

        val popularSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList()
            else runCatching {
                tmdbClient.getPopularSeries(language).results.map { 
                    it.toVideo(SERIES, tGenres, favorites.contains(it.id))
                }
            }.getOrElse { emptyList() }
        }

        val topRatedMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else runCatching {
                tmdbClient.getTopRatedMovies(language).results.map { 
                    it.toVideo(MOVIES, mGenres, favorites.contains(it.id))
                }
            }.getOrElse { emptyList() }
        }

        val topRatedSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList<Video>()
            else runCatching {
                tmdbClient.getTopRatedSeries(language).results.map { 
                    it.toVideo(SERIES, tGenres, favorites.contains(it.id))
                }
            }.getOrElse { emptyList() }
        }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val topRatedSeries = topRatedSeriesDeferred.await()

        val categoryVideosMap = genres.take(HomeConfig.MAX_CATEGORIES).associate { category ->
            category.name to async {
                runCatching {
                    if (type == HomeFilterType.SERIES) {
                        tmdbClient.getTvByGenre(category.id, language).results.map { 
                            it.toVideo(SERIES, tGenres, favorites.contains(it.id))
                        }
                    } else {
                        tmdbClient.getMoviesByGenre(category.id, language).results.map { 
                            it.toVideo(MOVIES, mGenres, favorites.contains(it.id))
                        }
                    }
                }.getOrElse { emptyList() }
            }
        }.mapValues { it.value.await() }

        HomeDomainData(
            banner = popularMovies.firstOrNull() ?: popularSeries.firstOrNull(),
            top10 = (topRatedMovies + topRatedSeries).sortedByDescending { it.rating ?: 0f }.take(HomeConfig.MAX_TOP_10),
            popular = (popularMovies + popularSeries).shuffled().take(HomeConfig.MAX_POPULAR),
            categories = categoryVideosMap
        )
    }
}
