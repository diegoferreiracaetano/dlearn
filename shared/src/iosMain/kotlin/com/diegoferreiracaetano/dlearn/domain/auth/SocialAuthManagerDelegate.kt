package com.diegoferreiracaetano.dlearn.domain.auth

/**
 * Interface que o Swift deve implementar para fornecer as funcionalidades reais.
 */
interface SocialAuthManagerDelegate {
    suspend fun googleSignIn(): SocialAuthResult

    suspend fun appleSignIn(): SocialAuthResult

    suspend fun facebookSignIn(): SocialAuthResult

    suspend fun signOut()
}
