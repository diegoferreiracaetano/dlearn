package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.data.auth.challenge.remote.ChallengeRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import org.koin.dsl.module

/**
 * Módulo do Koin específico para o contexto de Autenticação e Desafios (MFA).
 * Centraliza a lógica de segurança de forma independente para facilitar futura extração.
 */
val authModule = module {
    
    // Coordenador Global de Desafios (Agnóstico)
    single<ChallengeCoordinator> { ChallengeCoordinator(get()) }

    // Handlers de Desafio (Preparado para múltiplos handlers: OTP, Biometria, etc)
    single<OtpChallengeHandler> { OtpChallengeHandler(get()) }

    // Motor de Desafios (Challenge Engine)
    single<ChallengeEngine> { 
        ChallengeEngine(
            handlers = listOf(get<OtpChallengeHandler>())
        ) 
    }

    // Repositório de Desafios
    single<ChallengeRepository> { ChallengeRepositoryRemote(get()) }
}
