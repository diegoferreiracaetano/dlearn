package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.util.AttributeKey
import org.koin.ktor.ext.inject

val ChallengePreferenceKey = AttributeKey<ChallengeType>("ChallengePreference")

fun Application.configureStatusPages() {
    val challengeMapper by inject<ChallengeMapper>()

    install(StatusPages) {
        exception<AppException> { call, cause ->
            handleAppException(call, cause)
        }

        exception<SecurityException> { call, cause ->
            handleSecurityException(call, cause)
        }

        exception<Throwable> { call, cause ->
            handleGeneralThrowable(call, cause, challengeMapper)
        }
    }
}

private suspend fun handleAppException(
    call: ApplicationCall,
    cause: AppException,
) {
    val status = mapAppErrorCodeToHttpStatusCode(cause.error.code)
    call.respond(status, cause.error)
}

private suspend fun handleSecurityException(
    call: ApplicationCall,
    cause: SecurityException,
) {
    call.respond(
        status = HttpStatusCode.Unauthorized,
        message =
        AppError(
            code = AppErrorCode.UNAUTHORIZED,
            message = cause.message ?: "Unauthorized access",
        ),
    )
}

private suspend fun handleGeneralThrowable(
    call: ApplicationCall,
    cause: Throwable,
    challengeMapper: ChallengeMapper,
) {
    val preferredType = getPreferredChallengeType(call)
    val challenge = challengeMapper.toChallengeSession(cause, preferredType)

    if (challenge != null) {
        call.respond(
            HttpStatusCode.fromValue(Constants.HTTP_STATUS_PRECONDITION_REQUIRED),
            challenge
        )
    } else {
        val (status, code) = mapThrowableToErrorResponse(cause)
        call.respond(
            status = status,
            message =
            AppError(
                code = code,
                message = cause.message ?: "Internal server error",
            ),
        )
    }
}

private fun getPreferredChallengeType(call: ApplicationCall): ChallengeType? {
    val preferenceFromController = call.attributes.getOrNull(ChallengePreferenceKey)
    val preferenceHeader = call.request.header(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)

    return preferenceFromController
        ?: preferenceHeader?.let { header ->
            ChallengeType.entries.find { it.name == header }
        }
}

private fun mapAppErrorCodeToHttpStatusCode(code: AppErrorCode): HttpStatusCode =
    when (code) {
        AppErrorCode.UNAUTHORIZED,
        AppErrorCode.INVALID_TOKEN,
        AppErrorCode.EXPIRED_TOKEN,
        -> HttpStatusCode.Unauthorized

        AppErrorCode.FORBIDDEN -> HttpStatusCode.Forbidden

        AppErrorCode.NOT_FOUND,
        AppErrorCode.USER_NOT_FOUND,
        AppErrorCode.MOVIE_NOT_FOUND,
        -> HttpStatusCode.NotFound

        AppErrorCode.INVALID_CREDENTIALS -> HttpStatusCode.Unauthorized

        AppErrorCode.USER_ALREADY_EXISTS,
        AppErrorCode.EMAIL_ALREADY_IN_USE,
        -> HttpStatusCode.Conflict

        else -> HttpStatusCode.BadRequest
    }

private fun mapThrowableToErrorResponse(cause: Throwable): Pair<HttpStatusCode, AppErrorCode> =
    when (cause) {
        is IllegalArgumentException -> HttpStatusCode.BadRequest to AppErrorCode.BAD_REQUEST
        is SecurityException -> HttpStatusCode.Unauthorized to AppErrorCode.UNAUTHORIZED
        is NoSuchElementException -> HttpStatusCode.NotFound to AppErrorCode.NOT_FOUND
        else -> HttpStatusCode.InternalServerError to AppErrorCode.SERVER_ERROR
    }
