package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class PasswordOrchestratorTest {

    private val changePasswordUseCase = mockk<ChangePasswordUseCase>(relaxed = true)
    private val orchestrator = PasswordOrchestrator(changePasswordUseCase)

    @Test
    fun `given a change password request when changePassword is called should return the response from the use case`() = runTest {
        val request = mockk<ChangePasswordRequest>()
        val expectedResponse = mockk<ChangePasswordResponse>()
        coEvery { changePasswordUseCase.execute(request, "user1", "token") } returns expectedResponse

        val result = orchestrator.changePassword(request, "user1", "token").first()

        assertEquals(expectedResponse, result)
    }
}
