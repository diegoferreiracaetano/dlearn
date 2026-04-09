package com.diegoferreiracaetano.dlearn.domain.error

import kotlin.test.Test
import kotlin.test.assertEquals

class AppExceptionTest {

    @Test
    fun `given an AppError without message when AppException is created should use code name as message`() {
        val error = AppError(code = AppErrorCode.NOT_FOUND)
        val exception = AppException(error)

        assertEquals("NOT_FOUND", exception.message)
        assertEquals(AppErrorCode.NOT_FOUND, exception.error.code)
    }

    @Test
    fun `given an AppError with message when AppException is created should use that message`() {
        val customMessage = "Custom error message"
        val error = AppError(code = AppErrorCode.BAD_REQUEST, message = customMessage)
        val exception = AppException(error)

        assertEquals(customMessage, exception.message)
    }

    @Test
    fun `given an AppException with cause when created should preserve the cause`() {
        val cause = RuntimeException("Original cause")
        val error = AppError(code = AppErrorCode.UNKNOWN_ERROR)
        val exception = AppException(error, cause)

        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given an AppError with details when created should preserve details`() {
        val details = mapOf("field" to "email", "reason" to "invalid")
        val error = AppError(code = AppErrorCode.VALIDATION_FAILED, details = details)
        
        assertEquals(details, error.details)
    }

    @Test
    fun `AppErrorCode names should be accessible`() {
        assertEquals("NETWORK_ERROR", AppErrorCode.NETWORK_ERROR.name)
    }
}
