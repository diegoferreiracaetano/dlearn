package com.diegoferreiracaetano.dlearn.domain.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppErrorMappingTest {
    @Test
    fun `given generic exception when mapping to app error should return unknown error code with its message`() {
        val actual = RuntimeException("Random error").toAppError()

        assertEquals(AppErrorCode.UNKNOWN_ERROR, actual.code)
        assertEquals("Random error", actual.message)
    }

    @Test
    fun `given app exception when mapping to app error should return its internal error`() {
        val appError = AppError(AppErrorCode.NOT_FOUND, "Not found")

        val actual = AppException(appError).toAppError()

        assertEquals(appError, actual)
    }

    @Test
    fun `given retryable codes when checking can retry should be true`() {
        val retryable =
            listOf(
                AppErrorCode.NETWORK_ERROR,
                AppErrorCode.TIMEOUT,
                AppErrorCode.SERVER_ERROR,
                AppErrorCode.SERVICE_UNAVAILABLE,
            )

        retryable.forEach { code ->
            assertTrue(AppError(code).canRetry, "$code deveria permitir nova tentativa")
        }
    }

    @Test
    fun `given non retryable codes when checking can retry should be false`() {
        val retryable =
            setOf(
                AppErrorCode.NETWORK_ERROR,
                AppErrorCode.TIMEOUT,
                AppErrorCode.SERVER_ERROR,
                AppErrorCode.SERVICE_UNAVAILABLE,
            )

        AppErrorCode.entries.filterNot { it in retryable }.forEach { code ->
            assertFalse(AppError(code).canRetry, "$code não deveria permitir nova tentativa")
        }
    }
}
