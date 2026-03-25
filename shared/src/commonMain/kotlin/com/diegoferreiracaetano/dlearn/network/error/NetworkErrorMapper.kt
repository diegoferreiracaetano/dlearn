package com.diegoferreiracaetano.dlearn.network.error

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is ClientRequestException -> {
            val appError = runCatching { response.body<AppError>() }.getOrNull()
            val fallbackMessage = runCatching { response.bodyAsText() }.getOrNull() ?: message
            
            AppException(
                error = appError ?: AppError(
                    code = response.status.toAppErrorCode(),
                    message = fallbackMessage
                ),
                cause = this
            )
        }
        is ServerResponseException -> {
            val appError = runCatching { response.body<AppError>() }.getOrNull()
            val fallbackMessage = runCatching { response.bodyAsText() }.getOrNull() ?: message
            
            AppException(
                error = appError ?: AppError(
                    code = response.status.toAppErrorCode(),
                    message = fallbackMessage
                ),
                cause = this
            )
        }
        is RedirectResponseException -> {
            AppException(
                error = AppError(code = AppErrorCode.UNKNOWN_ERROR, message = message),
                cause = this
            )
        }
        is HttpRequestTimeoutException -> {
            AppException(
                error = AppError(code = AppErrorCode.TIMEOUT, message = message),
                cause = this
            )
        }
        is IOException -> {
            AppException(
                error = AppError(code = AppErrorCode.NETWORK_ERROR, message = message),
                cause = this
            )
        }
        is SerializationException -> {
            AppException(
                error = AppError(code = AppErrorCode.UNKNOWN_ERROR, message = "Error parsing response"),
                cause = this
            )
        }
        else -> {
            AppException(
                error = AppError(code = AppErrorCode.UNKNOWN_ERROR, message = message),
                cause = this
            )
        }
    }
}

private fun HttpStatusCode.toAppErrorCode(): AppErrorCode {
    return when (this.value) {
        HttpStatusCode.BadRequest.value -> AppErrorCode.BAD_REQUEST
        HttpStatusCode.Unauthorized.value -> AppErrorCode.UNAUTHORIZED
        HttpStatusCode.Forbidden.value -> AppErrorCode.FORBIDDEN
        HttpStatusCode.NotFound.value -> AppErrorCode.NOT_FOUND
        HttpStatusCode.RequestTimeout.value -> AppErrorCode.TIMEOUT
        HttpStatusCode.Conflict.value -> AppErrorCode.BAD_REQUEST
        HttpStatusCode.UnprocessableEntity.value -> AppErrorCode.VALIDATION_FAILED
        HttpStatusCode.InternalServerError.value -> AppErrorCode.SERVER_ERROR
        HttpStatusCode.ServiceUnavailable.value -> AppErrorCode.SERVICE_UNAVAILABLE
        HttpStatusCode.GatewayTimeout.value -> AppErrorCode.TIMEOUT
        428 -> AppErrorCode.CHALLENGE_REQUIRED
        else -> AppErrorCode.UNKNOWN_ERROR
    }
}
