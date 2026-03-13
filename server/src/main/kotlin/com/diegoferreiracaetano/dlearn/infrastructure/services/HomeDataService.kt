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
        userId: String,
        type: HomeFilterType = HomeFilterType.ALL
    ): HomeDomainData = coroutineScope {
        val genres = if (type == HomeFilterType.SERIES) tmdbClient.getTvGenres().genres
        else tmdbClient.getMovieGenres().genres

        val popularMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getPopularMovies().results.map { it.toVideo(MediaType.MOVIE) }
        }

        val popularSeriesDeferred = async {
            if (type == HomeFilterType.MOVIE) emptyList<Video>()
            else tmdbClient.getPopularSeries().results.map { it.toVideo(MediaType.SERIES) }
        }

        val topRatedMoviesDeferred = async {
            if (type == HomeFilterType.SERIES) emptyList<Video>()
            else tmdbClient.getTopRatedMovies().results.map { it.toVideo(MediaType.MOVIE) }
        }

        val topRatedSeriesDeferred = async {
            if (type == HomeFilterType.MOVIE) emptyList<Video>()
            else tmdbClient.getTopRatedSeries().results.map { it.toVideo(MediaType.SERIES) }
        }

        val popularMovies = popularMoviesDeferred.await()
        val popularSeries = popularSeriesDeferred.await()
        val topRatedMovies = topRatedMoviesDeferred.await()
        val topRatedSeries = topRatedSeriesDeferred.await()

        val categoryVideosMap = genres.take(4).associate { category ->
            category.name to async {
                if (type == HomeFilterType.SERIES) {
                    tmdbClient.getTvByGenre(category.id).results.map { it.toVideo(MediaType.SERIES) }
                } else {
                    tmdbClient.getMoviesByGenre(category.id).results.map { it.toVideo(MediaType.MOVIE) }
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
