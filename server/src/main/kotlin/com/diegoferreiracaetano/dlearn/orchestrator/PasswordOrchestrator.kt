package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.models.*
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordOrchestrator(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val passwordDataService: PasswordDataService,
    private val i18nProvider: I18nProvider
) {
    /**
     * Realiza a alteração de senha utilizando Flow.
     * Lança PasswordChallengeException se o desafio (MFA) for necessário.
     */
    fun changePassword(
        request: ChangePasswordRequest,
        challengeToken: String?,
        lang: String
    ): Flow<ChangePasswordResponse> = flow {
        if (challengeToken == null || !passwordDataService.validateChallenge(challengeToken)) {
            val newToken = passwordDataService.generateChallenge(request.userId)
            throw PasswordChallengeException(
                PasswordChallengeError(
                    code = PasswordChallengeCode.OTP_REQUIRED,
                    message = i18nProvider.getString(AppStringType.PASSWORD_OTP_REQUIRED, lang),
                    challengeToken = newToken
                )
            )
        }

        val response = changePasswordUseCase.execute(request, challengeToken)
        emit(response)
    }

    /**
     * Verifica o OTP e retorna o token validado.
     */
    fun verifyOtp(
        request: VerifyOtpRequest,
        challengeToken: String?,
        lang: String
    ): Flow<VerifyOtpResponse> = flow {
        if (challengeToken == null) {
            emit(
                VerifyOtpResponse(
                    success = false,
                    message = i18nProvider.getString(AppStringType.UPDATE_PROFILE_ERROR, lang)
                )
            )
            return@flow
        }

        val validatedToken = passwordDataService.verifyOtp(request.otpCode, challengeToken)
        val response = if (validatedToken != null) {
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
        emit(response)
    }
}

class PasswordChallengeException(val error: PasswordChallengeError) : RuntimeException(error.message)
