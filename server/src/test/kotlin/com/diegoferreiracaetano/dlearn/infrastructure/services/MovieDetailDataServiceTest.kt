package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import org.junit.After
import kotlin.test.assertEquals

class MovieDetailDataServiceTest {

    private val movieClient = mockk<MovieClient>(relaxed = true)
    private lateinit var service: MovieDetailDataService
    private lateinit var db: Database
    private val dbName = "test_movie_detail_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(FavoriteTable, WatchlistTable)
        }
        service = MovieDetailDataService(movieClient)
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(FavoriteTable, WatchlistTable)
        }
    }

    @Test
    fun `given a valid movieId when fetchMovieDetail is called should return movie detail with database states`() = runTest {
        val detail = MovieDetailDomainData(
            id = "1",
            title = "T",
            imageUrl = "I",
            year = "2024",
            duration = "2h",
            genre = "G",
            rating = "8",
            trailerId = "id",
            isFavorite = false,
            isInWatchlist = false,
            mediaType = MediaType.MOVIES,
            storyLine = "S",
            cast = emptyList(),
            providers = emptyList(),
            seasons = emptyList()
        )
        coEvery { movieClient.getMovieDetail("MOVIES_1", "en") } returns detail

        val result = service.fetchMovieDetail("MOVIES_1", "en", "user1")

        assertEquals("T", result.title)
        assertEquals(false, result.isFavorite)
        assertEquals(false, result.isInWatchlist)
    }
}
