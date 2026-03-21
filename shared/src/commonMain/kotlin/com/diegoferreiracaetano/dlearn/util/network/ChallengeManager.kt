package com.diegoferreiracaetano.dlearn.util.network

class ChallengeManager {
    private var challengeToken: String? = null

    fun saveToken(token: String?) {
        challengeToken = token
    }

    fun getChallengeToken(): String? = challengeToken

    fun clearToken() {
        challengeToken = null
    }
}
