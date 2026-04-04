package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state.VerifyAccountUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class VerifyAccountViewModelTest {

    private val challengeRepository = mockk<ChallengeRepository>(relaxed = true)
    private lateinit var viewModel: VerifyAccountViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = VerifyAccountViewModel(challengeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid otp when verifyOtp is called should update state to Success`() = runTest {
        val otp = "123456"
        coEvery { challengeRepository.resolveChallenge(otp) } returns flow {
            delay(100)
            emit(ChallengeResult.Success(emptyMap()))
        }

        viewModel.verifyOtp(otp)
        
        testDispatcher.scheduler.runCurrent()
        assertEquals(VerifyAccountUiState.Loading, viewModel.uiState.value)
        
        advanceUntilIdle()
        
        assertEquals(VerifyAccountUiState.Success, viewModel.uiState.value)
    }

    @Test
    fun `given invalid otp when verifyOtp is called should update state to Error`() = runTest {
        val otp = "wrong"
        val exception = RuntimeException("Invalid OTP")
        coEvery { challengeRepository.resolveChallenge(otp) } returns flowOf(ChallengeResult.Failure(exception))

        viewModel.verifyOtp(otp)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state is VerifyAccountUiState.Error)
        assertEquals(exception, state.throwable)
    }

    @Test
    fun `when resendOtp is called and succeeds should update state to Idle`() = runTest {
        coEvery { challengeRepository.resendChallenge() } returns flow {
            delay(100)
            emit(true)
        }

        viewModel.resendOtp()
        
        testDispatcher.scheduler.runCurrent()
        assertEquals(VerifyAccountUiState.Loading, viewModel.uiState.value)
        
        advanceUntilIdle()
        
        assertEquals(VerifyAccountUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `when resendOtp is called and fails should update state to Error`() = runTest {
        coEvery { challengeRepository.resendChallenge() } returns flowOf(false)

        viewModel.resendOtp()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state is VerifyAccountUiState.Error)
        assertEquals("Failed to resend OTP", state.throwable.message)
    }

    @Test
    fun `when cancel is called should call repository cancel`() {
        viewModel.cancel()
        coVerify(exactly = 1) { challengeRepository.cancelChallenge() }
    }
}
