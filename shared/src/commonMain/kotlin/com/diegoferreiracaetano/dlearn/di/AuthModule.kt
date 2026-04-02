package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.data.auth.challenge.remote.ChallengeRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.auth.SocialAuthManager
import com.diegoferreiracaetano.dlearn.domain.auth.SocialSignInUseCase
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import org.koin.dsl.module

val authModule =
    module {
        single { SessionManager(get()) }
        single { SocialAuthManager() }

        // Use Cases
        factory { SocialSignInUseCase(get(), get()) }

        // Challenge Engine & Coordinator
        single { ChallengeCoordinator(get()) }
        single { ChallengeEngine(get(), getAll()) }

        // Handlers de Desafio
        single { OtpChallengeHandler() }
        single<ChallengeHandler> { get<OtpChallengeHandler>() }

        // Repository
        single<ChallengeRepository> { ChallengeRepositoryRemote(get(), get(), get()) }
    }
