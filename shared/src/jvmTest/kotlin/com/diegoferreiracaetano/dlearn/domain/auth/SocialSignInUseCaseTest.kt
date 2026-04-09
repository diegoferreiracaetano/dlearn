package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SocialSignInUseCaseTest {

    private val socialAuthManager: SocialAuthManager = mockk()
    private val authRepository: AuthRepository = mockk()
    private val useCase = SocialSignInUseCase(socialAuthManager, authRepository)

    @Test
    fun `invoke with google success should call socialLogin`() = runTest {
        val successResult = SocialAuthResult.Success(idToken = "id", accessToken = "access")
        coEvery { socialAuthManager.googleSignIn() } returns successResult
        every { authRepository.socialLogin("google", "id", "access") } returns flowOf(mockk())

        val result = useCase.invoke(AccountProvider.GOOGLE).toList()

        assertEquals(1, result.size)
    }

    @Test
    fun `invoke with apple success should call socialLogin`() = runTest {
        val successResult = SocialAuthResult.Success(idToken = "id", accessToken = "access")
        coEvery { socialAuthManager.appleSignIn() } returns successResult
        every { authRepository.socialLogin("apple", "id", "access") } returns flowOf(mockk())

        val result = useCase.invoke(AccountProvider.APPLE).toList()

        assertEquals(1, result.size)
    }

    @Test
    fun `invoke with facebook success should call socialLogin`() = runTest {
        val successResult = SocialAuthResult.Success(idToken = "id", accessToken = "access")
        coEvery { socialAuthManager.facebookSignIn() } returns successResult
        every { authRepository.socialLogin("facebook", "id", "access") } returns flowOf(mockk())

        val result = useCase.invoke(AccountProvider.FACEBOOK).toList()

        assertEquals(1, result.size)
    }

    @Test
    fun `invoke with failure should throw AppException`() = runTest {
        val failureResult = SocialAuthResult.Failure(error = AppErrorCode.SOCIAL_AUTH_FAILED)
        coEvery { socialAuthManager.googleSignIn() } returns failureResult

        assertFailsWith<AppException> {
            useCase.invoke(AccountProvider.GOOGLE).first()
        }
    }

    @Test
    fun `invoke with cancelled should not emit`() = runTest {
        coEvery { socialAuthManager.googleSignIn() } returns SocialAuthResult.Cancelled

        val result = useCase.invoke(AccountProvider.GOOGLE).toList()

        assertEquals(0, result.size)
    }
}
