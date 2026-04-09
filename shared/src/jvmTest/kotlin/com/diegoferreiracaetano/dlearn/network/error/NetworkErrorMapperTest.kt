package com.diegoferreiracaetano.dlearn.network.error

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import org.junit.Test
import kotlin.test.assertEquals

class NetworkErrorMapperTest {

    @Test
    fun `given AppException should return itself`() = runTest {
        val appError = AppError(AppErrorCode.UNKNOWN_ERROR, "message")
        val exception = AppException(error = appError)
        val result = exception.toAppException()
        assertEquals(exception, result)
    }

    @Test
    fun `given ClientRequestException should map to AppException with BAD_REQUEST`() = runTest {
        val call = mockk<HttpClientCall>(relaxed = true)
        val response = mockk<HttpResponse>(relaxed = true)
        val request = mockk<HttpRequest>(relaxed = true)
        every { response.call } returns call
        every { call.response } returns response
        every { call.request } returns request
        every { response.status } returns HttpStatusCode.BadRequest

        val exception = ClientRequestException(response, "Bad Request")

        val result = exception.toAppException()
        assertEquals(AppErrorCode.BAD_REQUEST, result.error.code)
    }

    @Test
    fun `given ServerResponseException should map to AppException with SERVER_ERROR`() = runTest {
        val call = mockk<HttpClientCall>(relaxed = true)
        val response = mockk<HttpResponse>(relaxed = true)
        val request = mockk<HttpRequest>(relaxed = true)
        every { response.call } returns call
        every { call.response } returns response
        every { call.request } returns request
        every { response.status } returns HttpStatusCode.InternalServerError

        val exception = ServerResponseException(response, "Server Error")

        val result = exception.toAppException()
        assertEquals(AppErrorCode.SERVER_ERROR, result.error.code)
    }

    @Test
    fun `given RedirectResponseException should map to AppException with UNKNOWN_ERROR`() = runTest {
        val call = mockk<HttpClientCall>(relaxed = true)
        val response = mockk<HttpResponse>(relaxed = true)
        val request = mockk<HttpRequest>(relaxed = true)
        every { response.call } returns call
        every { call.response } returns response
        every { call.request } returns request
        every { response.status } returns HttpStatusCode.Found

        val exception = RedirectResponseException(response, "Redirect")

        val result = exception.toAppException()
        assertEquals(AppErrorCode.UNKNOWN_ERROR, result.error.code)
    }

    @Test
    fun `given HttpRequestTimeoutException should map to AppException with TIMEOUT`() = runTest {
        val exception = HttpRequestTimeoutException("url", 1000L)
        val result = exception.toAppException()
        assertEquals(AppErrorCode.TIMEOUT, result.error.code)
    }

    @Test
    fun `given IOException should map to AppException with NETWORK_ERROR`() = runTest {
        val exception = IOException("Network down")
        val result = exception.toAppException()
        assertEquals(AppErrorCode.NETWORK_ERROR, result.error.code)
    }

    @Test
    fun `given SerializationException should map to AppException with UNKNOWN_ERROR`() = runTest {
        val exception = SerializationException("Parse error")
        val result = exception.toAppException()
        assertEquals(AppErrorCode.UNKNOWN_ERROR, result.error.code)
        assertEquals("Error parsing response", result.error.message)
    }

    @Test
    fun `given any other exception should map to AppException with UNKNOWN_ERROR`() = runTest {
        val exception = RuntimeException("Unknown")
        val result = exception.toAppException()
        assertEquals(AppErrorCode.UNKNOWN_ERROR, result.error.code)
    }

    @Test
    fun `toAppErrorCode should map special status codes`() = runTest {
        val call = mockk<HttpClientCall>(relaxed = true)
        val response = mockk<HttpResponse>(relaxed = true)
        every { response.call } returns call
        every { call.response } returns response

        val statusCodes = mapOf(
            HttpStatusCode.Unauthorized to AppErrorCode.UNAUTHORIZED,
            HttpStatusCode.Forbidden to AppErrorCode.FORBIDDEN,
            HttpStatusCode.NotFound to AppErrorCode.NOT_FOUND,
            HttpStatusCode.RequestTimeout to AppErrorCode.TIMEOUT,
            HttpStatusCode.Conflict to AppErrorCode.BAD_REQUEST,
            HttpStatusCode.UnprocessableEntity to AppErrorCode.VALIDATION_FAILED,
            HttpStatusCode.ServiceUnavailable to AppErrorCode.SERVICE_UNAVAILABLE,
            HttpStatusCode.GatewayTimeout to AppErrorCode.TIMEOUT,
            HttpStatusCode(428, "Precondition Required") to AppErrorCode.CHALLENGE_REQUIRED
        )

        statusCodes.forEach { (status, expected) ->
            every { response.status } returns status
            val exception = ClientRequestException(response, "")
            val result = exception.toAppException()
            assertEquals(expected, result.error.code, "Failed for $status")
        }
    }
}
