package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

val ChallengePreferenceKey = AttributeKey<ChallengeType>("ChallengePreference")

fun Application.configureStatusPages() {

    val challengeMapper by inject<ChallengeMapper>()

    install(StatusPages) {

        exception<AppException> { call, cause ->
            val status = when (cause.error.code) {
                AppErrorCode.UNAUTHORIZED, AppErrorCode.INVALID_TOKEN, AppErrorCode.EXPIRED_TOKEN -> HttpStatusCode.Unauthorized
                AppErrorCode.FORBIDDEN -> HttpStatusCode.Forbidden
                AppErrorCode.NOT_FOUND, AppErrorCode.USER_NOT_FOUND, AppErrorCode.MOVIE_NOT_FOUND -> HttpStatusCode.NotFound
                AppErrorCode.INVALID_CREDENTIALS -> HttpStatusCode.Unauthorized
                AppErrorCode.USER_ALREADY_EXISTS, AppErrorCode.EMAIL_ALREADY_IN_USE -> HttpStatusCode.Conflict
                else -> HttpStatusCode.BadRequest
            }
            call.respond(status, cause.error)
        }

        exception<SecurityException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = AppError(
                    code = AppErrorCode.UNAUTHORIZED,
                    message = cause.message ?: "Unauthorized access"
                )
            )
        }

        exception<Throwable> { call, cause ->
            val preferenceFromController = call.attributes.getOrNull(ChallengePreferenceKey)
            val preferenceHeader = call.request.header(SecurityConstants.HEADER_CHALLENGE_PREFERENCE)

            val preferredType = preferenceFromController
                    ?: preferenceHeader?.let { header ->
                        ChallengeType.entries.find { it.name == header }
                    }

            val challenge = challengeMapper.toChallengeSession(cause, preferredType)

            if (challenge != null) {
                call.respond(HttpStatusCode.fromValue(428), challenge)
            } else {
                val (status, code) = when (cause) {
                    is IllegalArgumentException -> HttpStatusCode.BadRequest to AppErrorCode.BAD_REQUEST
                    is SecurityException -> HttpStatusCode.Unauthorized to AppErrorCode.UNAUTHORIZED
                    is NoSuchElementException -> HttpStatusCode.NotFound to AppErrorCode.NOT_FOUND
                    else -> HttpStatusCode.InternalServerError to AppErrorCode.SERVER_ERROR
                }

                call.respond(
                    status = status,
                    message = AppError(
                        code = code,
                        message = cause.message ?: "Internal server error"
                    )
                )
            }
        }
    }
}
