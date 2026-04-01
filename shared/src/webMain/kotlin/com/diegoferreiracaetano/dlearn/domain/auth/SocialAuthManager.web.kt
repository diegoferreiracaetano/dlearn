package com.diegoferreiracaetano.dlearn.domain.auth

actual open class SocialAuthManager actual constructor() {
    actual open suspend fun googleSignIn(): com.diegoferreiracaetano.dlearn.domain.auth.SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun appleSignIn(): com.diegoferreiracaetano.dlearn.domain.auth.SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun facebookSignIn(): com.diegoferreiracaetano.dlearn.domain.auth.SocialAuthResult {
        TODO("Not yet implemented")
    }

    actual open suspend fun signOut() {
    }
}