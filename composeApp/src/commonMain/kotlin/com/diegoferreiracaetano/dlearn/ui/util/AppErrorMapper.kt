package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.Composable
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.AppErrorData
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.AuthError
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.ChallengeError
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.GenericError
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.NoInternetError
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.NotFoundError
import com.diegoferreiracaetano.dlearn.designsystem.components.error.model.ServerError
import com.diegoferreiracaetano.dlearn.designsystem.util.rememberNetworkManager
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.error_challenge_expired
import dlearn.composeapp.generated.resources.error_challenge_required
import dlearn.composeapp.generated.resources.error_forbidden
import dlearn.composeapp.generated.resources.error_invalid_challenge_code
import dlearn.composeapp.generated.resources.error_invalid_credentials
import dlearn.composeapp.generated.resources.error_network
import dlearn.composeapp.generated.resources.error_not_found
import dlearn.composeapp.generated.resources.error_server
import dlearn.composeapp.generated.resources.error_service_unavailable
import dlearn.composeapp.generated.resources.error_social_auth_canceled
import dlearn.composeapp.generated.resources.error_social_auth_config_missing
import dlearn.composeapp.generated.resources.error_social_auth_failed
import dlearn.composeapp.generated.resources.error_timeout
import dlearn.composeapp.generated.resources.error_transaction_id_required
import dlearn.composeapp.generated.resources.error_unauthorized
import dlearn.composeapp.generated.resources.error_unknown
import dlearn.composeapp.generated.resources.error_unsupported_credential_type
import dlearn.composeapp.generated.resources.error_user_already_exists
import dlearn.composeapp.generated.resources.error_validation
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString


fun AppErrorCode.toResource(): StringResource {
    return when (this) {
        AppErrorCode.UNKNOWN_ERROR -> Res.string.error_unknown
        AppErrorCode.NETWORK_ERROR -> Res.string.error_network
        AppErrorCode.SERVER_ERROR -> Res.string.error_server
        AppErrorCode.BAD_REQUEST -> Res.string.error_validation
        AppErrorCode.UNAUTHORIZED -> Res.string.error_unauthorized
        AppErrorCode.FORBIDDEN -> Res.string.error_forbidden
        AppErrorCode.NOT_FOUND -> Res.string.error_not_found
        AppErrorCode.TIMEOUT -> Res.string.error_timeout
        AppErrorCode.INVALID_CREDENTIALS -> Res.string.error_invalid_credentials
        AppErrorCode.USER_NOT_FOUND -> Res.string.error_not_found
        AppErrorCode.USER_ALREADY_EXISTS -> Res.string.error_user_already_exists
        AppErrorCode.INVALID_TOKEN -> Res.string.error_unauthorized
        AppErrorCode.EXPIRED_TOKEN -> Res.string.error_unauthorized
        AppErrorCode.EMAIL_ALREADY_IN_USE -> Res.string.error_user_already_exists
        AppErrorCode.VALIDATION_FAILED -> Res.string.error_validation
        AppErrorCode.FIELD_REQUIRED -> Res.string.error_validation
        AppErrorCode.INVALID_FORMAT -> Res.string.error_validation
        AppErrorCode.MOVIE_NOT_FOUND -> Res.string.error_not_found
        AppErrorCode.SERVICE_UNAVAILABLE -> Res.string.error_service_unavailable
        
        // Social Auth Errors
        AppErrorCode.SOCIAL_AUTH_CONFIG_MISSING -> Res.string.error_social_auth_config_missing
        AppErrorCode.UNSUPPORTED_CREDENTIAL_TYPE -> Res.string.error_unsupported_credential_type
        AppErrorCode.SOCIAL_AUTH_FAILED -> Res.string.error_social_auth_failed
        AppErrorCode.SOCIAL_AUTH_CANCELED -> Res.string.error_social_auth_canceled

        // Challenge Errors
        AppErrorCode.CHALLENGE_REQUIRED -> Res.string.error_challenge_required
        AppErrorCode.INVALID_CHALLENGE_CODE -> Res.string.error_invalid_challenge_code
        AppErrorCode.CHALLENGE_EXPIRED -> Res.string.error_challenge_expired
        AppErrorCode.TRANSACTION_ID_REQUIRED -> Res.string.error_transaction_id_required

        else -> Res.string.error_unknown
    }
}

@Composable
fun AppError.toUiData(): AppErrorData {

    val networkManager = rememberNetworkManager()
    val isNetworkAvailable = networkManager.isNetworkAvailable()

    if (!isNetworkAvailable) return NoInternetError()

    return when (code) {

        // Auth
        AppErrorCode.UNAUTHORIZED,
        AppErrorCode.FORBIDDEN,
        AppErrorCode.INVALID_CREDENTIALS,
        AppErrorCode.INVALID_TOKEN,
        AppErrorCode.EXPIRED_TOKEN -> AuthError()

        // Not found
        AppErrorCode.NOT_FOUND,
        AppErrorCode.USER_NOT_FOUND,
        AppErrorCode.MOVIE_NOT_FOUND -> NotFoundError()

        // Challenge
        AppErrorCode.CHALLENGE_REQUIRED,
        AppErrorCode.INVALID_CHALLENGE_CODE,
        AppErrorCode.CHALLENGE_EXPIRED -> ChallengeError()

        // Server
        AppErrorCode.SERVER_ERROR,
        AppErrorCode.SERVICE_UNAVAILABLE,
        AppErrorCode.TIMEOUT -> ServerError()

        // Network
        AppErrorCode.NETWORK_ERROR -> NoInternetError()

        else -> GenericError()
    }
}

suspend fun Throwable.toAppMessage(): String {
    val error = this.toAppError()
    return error.message ?: getString(error.code.toResource())
}

fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppException -> this.error
        else -> AppError(
            code = AppErrorCode.UNKNOWN_ERROR,
            message = this.message
        )
    }
}
