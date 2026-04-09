package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppOrchestratorTest {

    private val homeOrchestrator = mockk<Orchestrator>()
    private val favoriteOrchestrator = mockk<Orchestrator>()
    private val watchlistOrchestrator = mockk<Orchestrator>()
    private val profileOrchestrator = mockk<Orchestrator>()
    private val faqOrchestrator = mockk<Orchestrator>()
    private val moviesOrchestrator = mockk<Orchestrator>()
    private val searchOrchestrator = mockk<Orchestrator>()
    private val welcomeOrchestrator = mockk<Orchestrator>()
    private val verifyAccountOrchestrator = mockk<Orchestrator>()
    private val settingsOrchestrator = mockk<Orchestrator>()
    private val usersOrchestrator = mockk<Orchestrator>()

    private val orchestrators = mapOf(
        AppNavigationRoute.HOME to homeOrchestrator,
        AppNavigationRoute.FAVORITE to favoriteOrchestrator,
        AppNavigationRoute.WATCHLIST to watchlistOrchestrator,
        AppNavigationRoute.PROFILE to profileOrchestrator,
        AppNavigationRoute.FAQ to faqOrchestrator,
        AppNavigationRoute.MOVIES to moviesOrchestrator,
        AppNavigationRoute.SEARCH to searchOrchestrator,
        AppNavigationRoute.WELCOME to welcomeOrchestrator,
        AppNavigationRoute.VERIFY_ACCOUNT to verifyAccountOrchestrator,
        AppNavigationRoute.SETTINGS_NOTIFICATIONS to settingsOrchestrator,
        AppNavigationRoute.USERS to usersOrchestrator,
    )

    private val appOrchestrator = AppOrchestrator(orchestrators)
    private val header = AppHeader()
    private val expectedScreen = flowOf(mockk<Screen>())

    private fun mockExecute(orchestrator: Orchestrator, path: String) {
        every { orchestrator.execute(AppRequest(path = path), header, "user1") } returns expectedScreen
    }

    @Test
    fun `given a home path when execute is called should delegate the request to the home orchestrator`() {
        val request = AppRequest(path = AppNavigationRoute.HOME)
        every { homeOrchestrator.execute(request, header, "user1") } returns expectedScreen

        val result = appOrchestrator.execute(request, header, "user1")

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a favorite path when execute is called should delegate to the favorite orchestrator`() {
        mockExecute(favoriteOrchestrator, AppNavigationRoute.FAVORITE)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.FAVORITE), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a movie favorite path when execute is called should delegate to the favorite orchestrator`() {
        mockExecute(favoriteOrchestrator, AppNavigationRoute.MOVIE_FAVORITE)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.MOVIE_FAVORITE), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a watchlist path when execute is called should delegate to the watchlist orchestrator`() {
        mockExecute(watchlistOrchestrator, AppNavigationRoute.WATCHLIST)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.WATCHLIST), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a movie watchlist path when execute is called should delegate to the watchlist orchestrator`() {
        mockExecute(watchlistOrchestrator, AppNavigationRoute.MOVIE_WATCHLIST)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.MOVIE_WATCHLIST), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a profile path when execute is called should delegate to the profile orchestrator`() {
        mockExecute(profileOrchestrator, AppNavigationRoute.PROFILE)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.PROFILE), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a profile edit path when execute is called should delegate to the profile orchestrator`() {
        mockExecute(profileOrchestrator, AppNavigationRoute.PROFILE_EDIT)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.PROFILE_EDIT), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a profile update path when execute is called should delegate to the profile orchestrator`() {
        mockExecute(profileOrchestrator, AppNavigationRoute.PROFILE_UPDATE)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.PROFILE_UPDATE), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a faq path when execute is called should delegate to the faq orchestrator`() {
        mockExecute(faqOrchestrator, AppNavigationRoute.FAQ)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.FAQ), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a movies path when execute is called should delegate to the movies orchestrator`() {
        mockExecute(moviesOrchestrator, AppNavigationRoute.MOVIES)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.MOVIES), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a search path when execute is called should delegate to the search orchestrator`() {
        mockExecute(searchOrchestrator, AppNavigationRoute.SEARCH)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.SEARCH), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a welcome path when execute is called should delegate to the welcome orchestrator`() {
        mockExecute(welcomeOrchestrator, AppNavigationRoute.WELCOME)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.WELCOME), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a verify account path when execute is called should delegate to the verify account orchestrator`() {
        mockExecute(verifyAccountOrchestrator, AppNavigationRoute.VERIFY_ACCOUNT)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.VERIFY_ACCOUNT), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a settings notifications path when execute is called should delegate to the settings orchestrator`() {
        mockExecute(settingsOrchestrator, AppNavigationRoute.SETTINGS_NOTIFICATIONS)
        val result = appOrchestrator.execute(
            AppRequest(path = AppNavigationRoute.SETTINGS_NOTIFICATIONS),
            header,
            "user1"
        )
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a settings language path when execute is called should delegate to the settings orchestrator`() {
        mockExecute(settingsOrchestrator, AppNavigationRoute.SETTINGS_LANGUAGE)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.SETTINGS_LANGUAGE), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a settings country path when execute is called should delegate to the settings orchestrator`() {
        mockExecute(settingsOrchestrator, AppNavigationRoute.SETTINGS_COUNTRY)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.SETTINGS_COUNTRY), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a users path when execute is called should delegate to the users orchestrator`() {
        mockExecute(usersOrchestrator, AppNavigationRoute.USERS)
        val result = appOrchestrator.execute(AppRequest(path = AppNavigationRoute.USERS), header, "user1")
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given an invalid path when execute is called should throw an IllegalArgumentException`() {
        assertFailsWith<IllegalArgumentException> {
            appOrchestrator.execute(AppRequest(path = "invalid"), header, "user1")
        }
    }
}
