package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
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
import kotlin.test.assertTrue

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
    fun `when changePassword is called and succeeds should update state to Success`() = runTest {
        val newPassword = "newPassword123"
        val response = ChangePasswordResponse(message = "Password changed successfully")

        coEvery { passwordRepository.changePassword(newPassword) } returns flow {
            delay(100)
            emit(response)
        }

        viewModel.changePassword(newPassword)

        testDispatcher.scheduler.runCurrent()
        assertEquals(CreateNewPasswordUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        assertEquals(CreateNewPasswordUiState.Success(response.message), viewModel.uiState.value)
    }

    @Test
    fun `when changePassword fails should update state to Error`() = runTest {
        val newPassword = "newPassword123"
        val errorMessage = "Error changing password"
        val exception = RuntimeException(errorMessage)

        coEvery { passwordRepository.changePassword(newPassword) } returns flow { throw exception }

        viewModel.changePassword(newPassword)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is CreateNewPasswordUiState.Error)
        assertEquals(errorMessage, state.message)
    }
}
