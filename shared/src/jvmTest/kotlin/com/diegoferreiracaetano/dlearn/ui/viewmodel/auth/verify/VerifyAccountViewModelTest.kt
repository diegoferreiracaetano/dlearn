package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.collectEffects
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
    fun `verifyOtp success should emit Verified and return to Idle`() = runTest {
        val otp = "123456"
        every { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Success(emptyMap()))
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(VerifyAccountAction.VerifyOtp(otp))

        assertEquals(VerifyAccountUiState.Idle, viewModel.uiState.value)
        assertEquals(listOf<VerifyAccountEffect>(VerifyAccountEffect.Verified), effects)
    }

    @Test
    fun `verifyOtp failure should update state to Error`() = runTest {
        val otp = "123456"
        val error = Throwable("Invalid OTP")
        every { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Failure(error))
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(VerifyAccountAction.VerifyOtp(otp))

        assertEquals(VerifyAccountUiState.Error(error.toAppError()), viewModel.uiState.value)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `verifyOtp cancelled should return to Idle`() = runTest {
        val otp = "123456"
        every { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Cancelled)

        viewModel.onAction(VerifyAccountAction.VerifyOtp(otp))

        assertEquals(VerifyAccountUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `resendOtp success should update state to Idle`() = runTest {
        every { challengeRepository.resendChallenge() } returns flowOf(true)

        viewModel.onAction(VerifyAccountAction.ResendOtp)

        assertEquals(VerifyAccountUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `resendOtp failure should update state to Error`() = runTest {
        every { challengeRepository.resendChallenge() } returns flowOf(false)

        viewModel.onAction(VerifyAccountAction.ResendOtp)

        assertEquals(
            VerifyAccountUiState.Error(AppError(AppErrorCode.UNKNOWN_ERROR, "Failed to resend OTP")),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `cancel should call repository cancelChallenge`() {
        viewModel.onAction(VerifyAccountAction.Cancel)
        coVerify { challengeRepository.cancelChallenge() }
    }
}
