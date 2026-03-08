package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeDataService(private val tmdbClient: TmdbClient) {
    suspend fun fetchHomeData(userId: String): HomeDomainData = coroutineScope {
        val popularMoviesDeferred = async { tmdbClient.getPopularMovies().results.map { it.toVideo(MediaType.MOVIE) } }
        val popularSeriesDeferred = async { tmdbClient.getPopularSeries().results.map { it.toVideo(MediaType.SERIES) } }
        val topRatedMoviesDeferred = async { tmdbClient.getTopRatedMovies().results.map { it.toVideo(MediaType.MOVIE) } }
        val genresDeferred = async { tmdbClient.getMovieGenres().genres }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val genres = genresDeferred.await()

        val categoryVideosMap = genres.take(4).associate { category ->
            category.name to async {
                tmdbClient.getMoviesByGenre(category.id).results.map {
                    it.toVideo(MediaType.MOVIE)
                }
            }
        }.mapValues { it.value.await() }

        HomeDomainData(
            banner = popularMovies.firstOrNull(),
            top10 = topRatedMovies.take(10),
            popular = (popularMovies + popularSeries).shuffled().take(10),
            categories = categoryVideosMap
        )
    }
}
