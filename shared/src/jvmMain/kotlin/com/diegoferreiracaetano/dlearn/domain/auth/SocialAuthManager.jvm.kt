package com.diegoferreiracaetano.dlearn.domain.auth

actual open class SocialAuthManager actual constructor() {
    actual open suspend fun googleSignIn(): SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun appleSignIn(): SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun facebookSignIn(): SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun signOut() {
    }
}
