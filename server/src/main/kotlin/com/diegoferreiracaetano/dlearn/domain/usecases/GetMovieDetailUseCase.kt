package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.*
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import java.util.Locale

class GetMovieDetailUseCase(private val tmdbClient: TmdbClient) {
    suspend fun execute(movieId: String): MovieDetailDomainData {
        val response = try {
            tmdbClient.getMovieDetail(movieId)
        } catch (_: Exception) {
            tmdbClient.getTvShowDetail(movieId)
        }
        
        return MovieDetailDomainData(
            id = response.id.toString(),
            title = response.title ?: response.name ?: "",
            imageUrl = "https://image.tmdb.org/t/p/w500${response.posterPath}",
            year = (response.releaseDate ?: response.firstAirDate ?: "").take(4),
            duration = response.runtime?.let { "$it Minutes" } ?: "",
            genre = response.genres.firstOrNull()?.name ?: "",
            rating = String.format(Locale.US, "%.1f", response.voteAverage ?: 0.0),
            storyLine = response.overview ?: "",
            cast = response.credits?.cast?.take(10)?.map {
                CastMemberDomainData(
                    id = it.id.toString(),
                    name = it.name,
                    role = it.character,
                    imageUrl = it.profilePath?.let { path -> "https://image.tmdb.org/t/p/w185$path" }
                )
            } ?: emptyList(),
            seasons = emptyList()
        )
    }
}
