package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.collectEffects
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateNewPasswordViewModelTest {

    private val passwordRepository = mockk<PasswordRepository>()
    private lateinit var viewModel: CreateNewPasswordViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateNewPasswordViewModel(passwordRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when changePassword succeeds should go through Loading then emit Changed`() = runTest {
        val newPassword = "newPassword123"
        val response = ChangePasswordResponse(message = "Password changed successfully")
        val effects = collectEffects(viewModel.effects)

        coEvery { passwordRepository.changePassword(newPassword) } returns flow {
            delay(100)
            emit(response)
        }

        viewModel.onAction(CreateNewPasswordAction.ChangePassword(newPassword))

        testDispatcher.scheduler.runCurrent()
        assertEquals(CreateNewPasswordUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        assertEquals(CreateNewPasswordUiState.Idle, viewModel.uiState.value)
        assertEquals(listOf<CreateNewPasswordEffect>(CreateNewPasswordEffect.Changed(response.message)), effects)
    }

    @Test
    fun `when changePassword fails should emit ShowError and return to Idle`() = runTest {
        val newPassword = "newPassword123"
        val exception = RuntimeException("Error changing password")
        coEvery { passwordRepository.changePassword(newPassword) } returns flow { throw exception }
        val effects = collectEffects(viewModel.effects)

        viewModel.onAction(CreateNewPasswordAction.ChangePassword(newPassword))
        advanceUntilIdle()

        assertEquals(CreateNewPasswordUiState.Idle, viewModel.uiState.value)
        assertEquals(
            listOf<CreateNewPasswordEffect>(CreateNewPasswordEffect.ShowError(exception.toAppError())),
            effects,
        )
    }
}
