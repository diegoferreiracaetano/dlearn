package com.diegoferreiracaetano.dlearn.domain.auth

/**
 * Implementação iOS do SocialAuthManager.
 * Utiliza um delegate que é definido pelo Swift.
 */
actual open class SocialAuthManager actual constructor() {
    
    companion object {
        var delegate: SocialAuthManagerDelegate? = null
    }

    actual open suspend fun googleSignIn(): SocialAuthResult {
        return delegate?.googleSignIn() ?: SocialAuthResult.Cancelled
    }

    actual open suspend fun appleSignIn(): SocialAuthResult {
        return delegate?.appleSignIn() ?: SocialAuthResult.Cancelled
    }

    actual open suspend fun facebookSignIn(): SocialAuthResult {
        return delegate?.facebookSignIn() ?: SocialAuthResult.Cancelled
    }

    actual open suspend fun signOut() {
        delegate?.signOut()
    }
}

/**
 * Interface que o Swift deve implementar para fornecer as funcionalidades reais.
 */
interface SocialAuthManagerDelegate {
    suspend fun googleSignIn(): SocialAuthResult
    suspend fun appleSignIn(): SocialAuthResult
    suspend fun facebookSignIn(): SocialAuthResult
    suspend fun signOut()
}
