package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

/**
 * Define a preferência de desafio para a requisição atual.
 *
 * O servidor usará esta informação para decidir qual método de desafio disparar
 * primeiro, caso múltiplos métodos estejam disponíveis para o usuário.
 *
 * @param type O tipo de desafio preferido (ex: OTP_SMS, BIOMETRIC).
 */
fun HttpRequestBuilder.challengePreference(type: ChallengeType) {
    header(SecurityConstants.HEADER_CHALLENGE_PREFERENCE, type.name)
}
