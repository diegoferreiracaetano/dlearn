package com.diegoferreiracaetano.dlearn.service

import com.diegoferreiracaetano.dlearn.model.CategoryItems
import com.diegoferreiracaetano.dlearn.model.HomeData
import com.diegoferreiracaetano.dlearn.model.HomeForClient
import com.diegoferreiracaetano.dlearn.model.LayoutSection
import com.diegoferreiracaetano.dlearn.model.SectionType
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.util.Cache
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class HomeService {
    private val tmdbClient = TmdbClient()
    private val cache = Cache<HomeForClient>(300_000L) // 300s TTL

    suspend fun getHome(): HomeForClient {
        val cachedHome = cache.get("home")
        if (cachedHome != null) {
            return cachedHome
        }

        return coroutineScope {
            val popularMoviesDeferred = async { tmdbClient.getPopularMovies().results }
            val topRatedMoviesDeferred = async { tmdbClient.getTopRatedMovies().results }
            val genresDeferred = async { tmdbClient.getMovieGenres().genres }

            val popularMovies = popularMoviesDeferred.await()
            val topRatedMovies = topRatedMoviesDeferred.await()
            val genres = genresDeferred.await()

            val bannerMain = popularMovies.firstOrNull()

            // Fetch the first 4 genres and their movies concurrently
            val categoryItems = genres.take(4).map { category ->
                async {
                    val movies = tmdbClient.getMoviesByGenre(category.id).results
                    CategoryItems(category, movies)
                }
            }.awaitAll()

            val layout = mutableListOf<LayoutSection>()
            if (bannerMain != null) {
                layout.add(LayoutSection(SectionType.BANNER_MAIN, "BANNER_MAIN"))
            }
            if (topRatedMovies.isNotEmpty()) {
                layout.add(LayoutSection(SectionType.TOP_10, "TOP_10", "Top 10"))
            }
            if (popularMovies.isNotEmpty()) {
                layout.add(LayoutSection(SectionType.POPULAR, "POPULAR", "Populares"))
            }
            categoryItems.forEach {
                layout.add(LayoutSection(SectionType.CATEGORY, "CATEGORY_${it.category.id}", it.category.name))
            }


            val homeData = HomeData(
                bannerMain = bannerMain,
                top10 = topRatedMovies,
                popular = popularMovies,
                categories = categoryItems
            )

            val homeForClient = HomeForClient(layout, homeData)
            cache.put("home", homeForClient)
            homeForClient
        }
    }
}
