package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProfileOrchestratorTest {

    private lateinit var subject: ProfileOrchestrator
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val screenBuilder = mockk<ProfileScreenBuilder>(relaxed = true)
    private val editScreenBuilder = mockk<EditProfileScreenBuilder>(relaxed = true)
    private val cacheManager = mockk<CacheManager>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        startKoin {
            modules(module {
                single { cacheManager }
            })
        }
        every { cacheManager.get<Screen>(any(), any()) } returns null
        subject = ProfileOrchestrator(userRepository, screenBuilder, editScreenBuilder)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `given a profile path when execute is called should return the profile screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.PROFILE)
        val user = mockk<User>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { userRepository.findById(userId) } returns user
        every { screenBuilder.build(user, lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).toList()

        assertEquals(expectedScreen, result.last())
    }

    @Test
    fun `given a profile path and user is not found when execute is called should throw an AppException`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.PROFILE)

        coEvery { userRepository.findById(userId) } returns null

        assertFailsWith<AppException> {
            subject.execute(request, header, userId).toList()
        }
    }

    @Test
    fun `given a profile edit path when execute is called should return the edit profile screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.PROFILE_EDIT)
        val user = mockk<User>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { userRepository.findById(userId) } returns user
        every { editScreenBuilder.build(user, lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).toList()

        assertEquals(expectedScreen, result.last())
    }

    @Test
    fun `given a profile update path when execute is called should return a success edit profile screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.PROFILE_UPDATE)
        val user = mockk<User>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { userRepository.findById(userId) } returns user
        every {
            editScreenBuilder.build(
                data = user,
                lang = lang,
                status = AppStringType.UPDATE_PROFILE_SUCCESS,
                type = AppSnackbarType.SUCCESS
            )
        } returns expectedScreen

        val result = subject.execute(request, header, userId).toList()

        assertEquals(expectedScreen, result.last())
    }

    @Test
    fun `given a profile update path and an error occurs when execute is called should return an error edit profile screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.PROFILE_UPDATE)
        val user = mockk<User>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { userRepository.findById(userId) } throws RuntimeException() andThen user
        every {
            editScreenBuilder.build(
                data = user,
                lang = lang,
                status = AppStringType.UPDATE_PROFILE_ERROR,
                type = AppSnackbarType.ERROR
            )
        } returns expectedScreen

        val result = subject.execute(request, header, userId).toList()

        assertEquals(expectedScreen, result.last())
    }

    @Test
    fun `given an invalid path when execute is called should throw an IllegalArgumentException`() = runTest {
        val request = AppRequest(path = "invalid/path")

        assertFailsWith<IllegalArgumentException> {
            subject.execute(request, header, userId).toList()
        }
    }
}
