package com.diegoferreiracaetano.dlearn.domain.error

fun Throwable.toAppError(): AppError =
    when (this) {
        is AppException -> error
        else ->
            AppError(
                code = AppErrorCode.UNKNOWN_ERROR,
                message = message,
            )
    }

private val RETRYABLE_CODES =
    setOf(
        AppErrorCode.NETWORK_ERROR,
        AppErrorCode.TIMEOUT,
        AppErrorCode.SERVER_ERROR,
        AppErrorCode.SERVICE_UNAVAILABLE,
    )

val AppError.canRetry: Boolean
    get() = code in RETRYABLE_CODES
