package com.diegoferreiracaetano.dlearn.domain.challenge

interface ChallengeHandler {
    fun canHandle(challenge: Challenge): Boolean
    suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult
}
