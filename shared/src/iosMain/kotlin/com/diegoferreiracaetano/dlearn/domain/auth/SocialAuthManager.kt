package com.diegoferreiracaetano.dlearn.domain.auth

/**
 * Implementação iOS do SocialAuthManager.
 * Utiliza um delegate que é definido pelo Swift.
 */
actual open class SocialAuthManager actual constructor() {
    companion object {
        var delegate: SocialAuthManagerDelegate? = null
    }

    actual open suspend fun googleSignIn(): SocialAuthResult =
        delegate?.googleSignIn() ?: SocialAuthResult.Cancelled

    actual open suspend fun appleSignIn(): SocialAuthResult =
        delegate?.appleSignIn() ?: SocialAuthResult.Cancelled

    actual open suspend fun facebookSignIn(): SocialAuthResult =
        delegate?.facebookSignIn() ?: SocialAuthResult.Cancelled

    actual open suspend fun signOut() {
        delegate?.signOut()
    }
}
