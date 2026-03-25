package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.data.auth.challenge.remote.ChallengeRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import org.koin.dsl.module

val authModule = module {
    single { SessionManager(get()) }
    
    // Challenge Engine & Coordinator
    single { ChallengeCoordinator(get()) }
    single { ChallengeEngine(get(), getAll()) }
    
    // Handlers de Desafio
    single { OtpChallengeHandler() }
    single<ChallengeHandler> { get<OtpChallengeHandler>() }
    // O BiometricChallengeHandler será injetado pelo platformAuthModule se disponível

    // Repository
    single<ChallengeRepository> { ChallengeRepositoryRemote(get(), get(), get()) }
}

expect val platformAuthModule: org.koin.core.module.Module
