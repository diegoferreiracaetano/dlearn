package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.models.*
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class PasswordOrchestrator(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val passwordDataService: PasswordDataService,
    private val i18nProvider: I18nProvider
) {
    fun changePassword(
        request: ChangePasswordRequest,
        challengeToken: String?,
        lang: String
    ): Result<ChangePasswordResponse> {
        if (challengeToken == null || !passwordDataService.validateChallenge(challengeToken)) {
            val newToken = passwordDataService.generateChallenge(request.userId)
            return Result.failure(
                PasswordChallengeException(
                    PasswordChallengeError(
                        code = PasswordChallengeCode.OTP_REQUIRED,
                        message = i18nProvider.getString(AppStringType.PASSWORD_OTP_REQUIRED, lang),
                        challengeToken = newToken
                    )
                )
            )
        }

        return try {
            Result.success(changePasswordUseCase.execute(request, challengeToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun verifyOtp(request: VerifyOtpRequest, challengeToken: String?, lang: String): VerifyOtpResponse {
        if (challengeToken == null) {
            return VerifyOtpResponse(
                success = false,
                message = i18nProvider.getString(AppStringType.UPDATE_PROFILE_ERROR, lang)
            )
        }

        val validatedToken = passwordDataService.verifyOtp(request.otpCode, challengeToken)
        return if (validatedToken != null) {
            VerifyOtpResponse(
                success = true,
                message = i18nProvider.getString(AppStringType.PASSWORD_OTP_VERIFIED, lang),
                validatedToken = validatedToken
            )
        } else {
            VerifyOtpResponse(
                success = false,
                message = i18nProvider.getString(AppStringType.UPDATE_PROFILE_ERROR, lang)
            )
        }
    }
}

class PasswordChallengeException(val error: PasswordChallengeError) : RuntimeException(error.message)
