package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ChangePasswordUseCaseTest {

    private val passwordDataService = mockk<PasswordDataService>(relaxed = true)
    private val useCase = ChangePasswordUseCase(passwordDataService)

    @Test
    fun `given a change password request when execute is called should return the response from service`() = runTest {
        val request = mockk<ChangePasswordRequest>()
        val expectedResponse = mockk<ChangePasswordResponse>()
        coEvery { passwordDataService.changePassword(request, "user1", "token") } returns expectedResponse

        val result = useCase.execute(request, "user1", "token")

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `given a null challenge token when execute is called should delegate to service with null token`() = runTest {
        val request = mockk<ChangePasswordRequest>()
        val expectedResponse = mockk<ChangePasswordResponse>()
        coEvery { passwordDataService.changePassword(request, "user1", null) } returns expectedResponse

        val result = useCase.execute(request, "user1", null)

        assertEquals(expectedResponse, result)
    }
}
