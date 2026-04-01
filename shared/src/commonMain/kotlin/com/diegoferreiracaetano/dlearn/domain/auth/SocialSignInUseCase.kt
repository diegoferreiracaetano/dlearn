package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SocialSignInUseCase(
    private val socialAuthManager: SocialAuthManager,
    private val authRepository: AuthRepository
) : UseCase<AccountProvider, Flow<Unit>> {

    override fun invoke(params: AccountProvider): Flow<Unit> = flow {
        val result = when (params) {
            AccountProvider.GOOGLE -> socialAuthManager.googleSignIn()
            AccountProvider.APPLE -> socialAuthManager.appleSignIn()
            AccountProvider.FACEBOOK -> socialAuthManager.facebookSignIn()
        }

        when (result) {
            is SocialAuthResult.Success -> {
                authRepository.socialLogin(
                    provider = params.name.lowercase(),
                    idToken = result.idToken,
                    accessToken = result.accessToken
                ).collect {
                    emit(Unit)
                }
            }
            is SocialAuthResult.Failure -> {
                throw AppException(AppError(code = result.error))
            }
            is SocialAuthResult.Cancelled -> {
                // Silencioso ou emitir evento de cancelamento se necessário
            }
        }
    }
}
