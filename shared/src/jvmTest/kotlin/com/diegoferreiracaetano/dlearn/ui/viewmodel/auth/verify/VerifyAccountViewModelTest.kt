package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class VerifyAccountViewModelTest {

    private val challengeRepository = mockk<ChallengeRepository>(relaxed = true)
    private lateinit var viewModel: VerifyAccountViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = VerifyAccountViewModel(challengeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verifyOtp success should update state to Success`() = runTest {
        val otp = "123456"
        every { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Success(emptyMap()))

        viewModel.verifyOtp(otp)

        assertEquals(VerifyAccountUiState.Success, viewModel.uiState.value)
    }

    @Test
    fun `verifyOtp failure should update state to Error`() = runTest {
        val otp = "123456"
        val error = Throwable("Invalid OTP")
        every { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Failure(error))

        viewModel.verifyOtp(otp)

        assertTrue(viewModel.uiState.value is VerifyAccountUiState.Error)
        assertEquals(error, (viewModel.uiState.value as VerifyAccountUiState.Error).throwable)
    }

    @Test
    fun `resendOtp success should update state to Idle`() = runTest {
        every { challengeRepository.resendChallenge() } returns flowOf(true)

        viewModel.resendOtp()

        assertEquals(VerifyAccountUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `resendOtp failure should update state to Error`() = runTest {
        every { challengeRepository.resendChallenge() } returns flowOf(false)

        viewModel.resendOtp()

        assertTrue(viewModel.uiState.value is VerifyAccountUiState.Error)
        assertEquals("Failed to resend OTP", (viewModel.uiState.value as VerifyAccountUiState.Error).throwable.message)
    }

    @Test
    fun `cancel should call repository cancelChallenge`() {
        viewModel.cancel()
        coVerify { challengeRepository.cancelChallenge() }
    }
}
