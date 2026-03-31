package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
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
        val socialResult = when (params) {
            AccountProvider.GOOGLE -> socialAuthManager.googleSignIn()
            AccountProvider.APPLE -> socialAuthManager.appleSignIn()
            AccountProvider.FACEBOOK -> socialAuthManager.facebookSignIn()
        }

        when (socialResult) {
            is SocialAuthResult.Success -> {
                authRepository.socialLogin(
                    provider = params.name.lowercase(),
                    idToken = socialResult.idToken,
                    accessToken = socialResult.accessToken
                ).collect {
                    emit(Unit)
                }
            }
            is SocialAuthResult.Error -> {
                throw AppException(AppError(code = socialResult.error))
            }
            SocialAuthResult.Cancelled -> {
                // Silencioso ou lançar exceção específica se necessário
            }
        }
    }
}
