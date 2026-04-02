package com.diegoferreiracaetano.dlearn.domain.auth

expect open class SocialAuthManager() {
    open suspend fun googleSignIn(): SocialAuthResult

    open suspend fun appleSignIn(): SocialAuthResult

    open suspend fun facebookSignIn(): SocialAuthResult

    open suspend fun signOut()
}
