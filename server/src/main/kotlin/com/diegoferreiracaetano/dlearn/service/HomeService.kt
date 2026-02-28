package com.diegoferreiracaetano.dlearn.service

import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeDataContent
import com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.util.Cache
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class HomeService {
    private val tmdbClient = TmdbClient()
    private val cache = Cache<Home>(300_000L) // 300s TTL

    suspend fun getHome(): Home {
        val cachedHome = cache.get("home")
        if (cachedHome != null) {
            return cachedHome
        }

        return coroutineScope {
            val popularMoviesDeferred = async { tmdbClient.getPopularMovies().results.map { it.toVideo(MediaType.MOVIE) } }
            val popularSeriesDeferred = async { tmdbClient.getPopularSeries().results.map { it.toVideo(MediaType.SERIES) } }
            val topRatedMoviesDeferred = async { tmdbClient.getTopRatedMovies().results.map { it.toVideo(MediaType.MOVIE) } }
            val genresDeferred = async { tmdbClient.getMovieGenres().genres }

            val popularMovies = popularMoviesDeferred.await()
            val popularSeries = popularSeriesDeferred.await()
            val topRatedMovies = topRatedMoviesDeferred.await()
            val genres = genresDeferred.await()

            val bannerMain = popularMovies.firstOrNull()?.copy(section = HomeSectionType.BANNER_MAIN)
            val top10 = topRatedMovies.take(10).map { it.copy(section = HomeSectionType.TOP_10) }
            val popular = (popularMovies + popularSeries).shuffled().map { it.copy(section = HomeSectionType.POPULAR) }

            val categoryItems = genres.take(4).map { category ->
                async {
                    tmdbClient.getMoviesByGenre(category.id).results.map {
                        it.toVideo(MediaType.MOVIE).copy(
                            section = HomeSectionType.CATEGORY,
                            category = category
                        )
                    }
                }
            }.awaitAll()

            val allItems = mutableListOf<Video>()
            bannerMain?.let { allItems.add(it) }
            allItems.addAll(top10)
            allItems.addAll(popular)
            categoryItems.forEach { allItems.addAll(it) }

            val uniqueItems = allItems.distinctBy { "${it.id}_${it.section}_${it.category?.id}" }

            val sections = mutableListOf<HomeSectionType>()
            if (bannerMain != null) {
                sections.add(HomeSectionType.BANNER_MAIN)
            }
            if (top10.isNotEmpty()) {
                sections.add(HomeSectionType.TOP_10)
            }
            if (popular.isNotEmpty()) {
                sections.add(HomeSectionType.POPULAR)
            }
            if (categoryItems.isNotEmpty()) {
                sections.add(HomeSectionType.CATEGORY)
            }

            val homeData = HomeDataContent(items = uniqueItems)

            val home = Home(sections, homeData)
            cache.put("home", home)
            home
        }
    }
}
