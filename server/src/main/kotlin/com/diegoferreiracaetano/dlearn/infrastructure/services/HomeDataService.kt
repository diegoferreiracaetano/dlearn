package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeDataService(private val tmdbClient: TmdbClient) {
    suspend fun fetchHomeData(
        language: String,
        type: HomeFilterType = HomeFilterType.ALL
    ): HomeDomainData = coroutineScope {
        val movieGenres = async { tmdbClient.getMovieGenres(language).genres }
        val tvGenres = async { tmdbClient.getTvGenres(language).genres }

        val genres = if (type == HomeFilterType.SERIES) tvGenres.await()
        else movieGenres.await()

        val popularMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getPopularMovies(language).results.map { it.toVideo(MediaType.MOVIE, movieGenres.await()) }
        }

        val popularSeriesDeferred = async {
            if (type == HomeFilterType.MOVIE) emptyList<Video>()
            else tmdbClient.getPopularSeries(language).results.map { it.toVideo(MediaType.SERIES, tvGenres.await()) }
        }

        val topRatedMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getTopRatedMovies(language).results.map { it.toVideo(MediaType.MOVIE, movieGenres.await()) }
        }

        val topRatedSeriesDeferred = async {
            if (type == HomeFilterType.MOVIE) emptyList<Video>()
            else tmdbClient.getTopRatedSeries(language).results.map { it.toVideo(MediaType.SERIES, tvGenres.await()) }
        }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val topRatedSeries = topRatedSeriesDeferred.await()

        val categoryVideosMap = genres.take(4).associate { category ->
            category.name to async {
                if (type == HomeFilterType.SERIES) {
                    tmdbClient.getTvByGenre(category.id, language).results.map { it.toVideo(MediaType.SERIES, tvGenres.await()) }
                } else {
                    tmdbClient.getMoviesByGenre(category.id, language).results.map { it.toVideo(MediaType.MOVIE, movieGenres.await()) }
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
