package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovieDetailDataServiceTest {

    private val movieClient = mockk<MovieClient>(relaxed = true)
    private lateinit var service: MovieDetailDataService
    private lateinit var db: Database
    private val dbName = "test_movie_detail_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;MODE=MySQL", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(UserTable, FavoriteTable, WatchlistTable)
        }
        service = MovieDetailDataService(movieClient)
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(WatchlistTable, FavoriteTable, UserTable)
        }
    }

    private fun createUser(userId: String) {
        transaction(db) {
            UserTable.insert {
                it[id] = userId
                it[name] = "Test User"
                it[email] = "$userId@test.com"
                it[password] = "pass"
            }
        }
    }

    @Test
    fun `given a valid movieId when fetchMovieDetail is called should return movie detail with database states`() = runTest {
        createUser("user1")
        val detail = MovieDetailDomainData(
            id = "MOVIES_1",
            title = "Inception",
            imageUrl = "I",
            year = "2010",
            duration = "148",
            genre = "Action",
            rating = "8.8",
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

        assertEquals("Inception", result.title)
        assertEquals(false, result.isFavorite)
        assertEquals(false, result.isInWatchlist)
    }

    @Test
    fun `given a favorite and watchlisted item when fetchMovieDetail is called should return detail with true flags`() = runTest {
        val userId = "user2"
        val mediaId = 123
        val mediaType = MediaType.SERIES
        createUser(userId)
        
        transaction(db) {
            FavoriteTable.insert {
                it[this.userId] = userId
                it[this.mediaId] = mediaId
                it[this.mediaType] = mediaType.name
            }
            WatchlistTable.insert {
                it[this.userId] = userId
                it[this.mediaId] = mediaId
                it[this.mediaType] = mediaType.name
            }
        }

        val detail = MovieDetailDomainData(
            id = "SERIES_123",
            title = "Breaking Bad",
            imageUrl = "I",
            year = "2008",
            duration = "45",
            genre = "Drama",
            rating = "9.5",
            trailerId = "id",
            isFavorite = false,
            isInWatchlist = false,
            mediaType = MediaType.SERIES,
            storyLine = "S",
            cast = emptyList(),
            providers = emptyList(),
            seasons = emptyList()
        )
        coEvery { movieClient.getMovieDetail("SERIES_123", "en") } returns detail

        val result = service.fetchMovieDetail("SERIES_123", "en", userId)

        assertTrue(result.isFavorite)
        assertTrue(result.isInWatchlist)
    }
}
