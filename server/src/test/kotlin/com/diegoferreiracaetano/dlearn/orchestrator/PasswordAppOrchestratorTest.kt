package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PasswordAppOrchestratorTest {

    @Test
    fun `when challenge token is missing should return failure with OTP_REQUIRED`() {
        val service = StubPasswordDataService()
        val useCase = ChangePasswordUseCase(service)
        val orchestrator = PasswordOrchestrator(useCase, service)
        val request = ChangePasswordRequest("user1", "old", "new")
        
        val result = orchestrator.changePassword(request, null)
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as PasswordChallengeException
        assertEquals("OTP_REQUIRED", exception.error.code)
    }

    @Test
    fun `when challenge token is invalid should return failure with OTP_REQUIRED`() {
        val service = StubPasswordDataService(isValidToken = false)
        val useCase = ChangePasswordUseCase(service)
        val orchestrator = PasswordOrchestrator(useCase, service)
        val request = ChangePasswordRequest("user1", "old", "new")
        
        val result = orchestrator.changePassword(request, "invalid-token")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as PasswordChallengeException
        assertEquals("OTP_REQUIRED", exception.error.code)
    }

    @Test
    fun `when challenge token is valid should return success`() {
        val service = StubPasswordDataService(isValidToken = true)
        val useCase = ChangePasswordUseCase(service)
        val orchestrator = PasswordOrchestrator(useCase, service)
        val request = ChangePasswordRequest("user1", "old", "new")
        
        val result = orchestrator.changePassword(request, "valid-otp-token")
        
        assertTrue(result.isSuccess)
        assertEquals("Password changed successfully", result.getOrNull()?.message)
    }

    private class StubPasswordDataService(private val isValidToken: Boolean = false) : PasswordDataService() {
        override fun validateChallenge(challengeToken: String): Boolean {
            return isValidToken && challengeToken == "valid-otp-token"
        }
    }
}
