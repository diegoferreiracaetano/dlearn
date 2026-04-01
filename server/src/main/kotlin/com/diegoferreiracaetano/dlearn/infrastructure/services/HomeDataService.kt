package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants.HomeConfig
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeDataService(
    private val movieClient: MovieClient,
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun fetchHomeData(
        language: String,
        type: HomeFilterType = HomeFilterType.ALL,
        userId: String
    ): HomeDomainData = coroutineScope {

        val favorites = runCatching {
            favoriteRepository.getFavorites(userId)
        }.getOrElse { emptyList() }

        val movieGenresDeferred = async { runCatching { movieClient.getMovieGenres(language) }.getOrElse { emptyList() } }
        val tvGenresDeferred = async { runCatching { movieClient.getTvGenres(language) }.getOrElse { emptyList() } }

        val mGenres = movieGenresDeferred.await()
        val tGenres = tvGenresDeferred.await()

        val genres = if (type == HomeFilterType.SERIES) tGenres else mGenres

        val popularMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else runCatching { 
                movieClient.getPopularMovies(language)
            }.getOrElse { emptyList() }.applyFavorites(favorites)
        }

        val popularSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList()
            else runCatching {
                movieClient.getPopularSeries(language)
            }.getOrElse { emptyList() }.applyFavorites(favorites)
        }

        val topRatedMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else runCatching {
                movieClient.getTopRatedMovies(language)
            }.getOrElse { emptyList() }.applyFavorites(favorites)
        }

        val topRatedSeriesDeferred = async {
            if (type == HomeFilterType.MOVIES) emptyList<Video>()
            else runCatching {
                movieClient.getTopRatedSeries(language)
            }.getOrElse { emptyList() }.applyFavorites(favorites)
        }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val topRatedSeries = topRatedSeriesDeferred.await()

        val categoryVideosMap = genres.take(HomeConfig.MAX_CATEGORIES).associate { category ->
            category.name to async {
                runCatching {
                    if (type == HomeFilterType.SERIES) {
                        movieClient.getTvByGenre(category.id, language)
                    } else {
                        movieClient.getMoviesByGenre(category.id, language)
                    }
                }.getOrElse { emptyList() }.applyFavorites(favorites)
            }
        }.mapValues { it.value.await() }

        HomeDomainData(
            banner = popularMovies.firstOrNull() ?: popularSeries.firstOrNull(),
            top10 = (topRatedMovies + topRatedSeries).sortedByDescending { it.rating ?: 0f }.take(HomeConfig.MAX_TOP_10),
            popular = (popularMovies + popularSeries).shuffled().take(HomeConfig.MAX_POPULAR),
            categories = categoryVideosMap
        )
    }

    private fun List<Video>.applyFavorites(favorites: List<String>): List<Video> {
        return map { video ->
            video.copy(isFavorite = favorites.contains(video.id))
        }
    }
}
