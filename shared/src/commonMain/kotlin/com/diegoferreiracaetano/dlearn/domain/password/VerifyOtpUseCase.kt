package com.diegoferreiracaetano.dlearn.domain.password

import com.diegoferreiracaetano.dlearn.data.challenge.OtpChallengeHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class VerifyOtpUseCase(
    private val repository: PasswordRepository,
    private val otpHandler: OtpChallengeHandler
) {
    operator fun invoke(request: VerifyOtpRequest): Flow<VerifyOtpResponse> =
        repository.verifyOtp(request).onEach { response ->
            if (response.success && response.validatedToken != null) {
                // Notifica o ChallengeEngine que o OTP foi resolvido com sucesso
                otpHandler.onChallengeResolved(response.validatedToken)
            }
        }
}
