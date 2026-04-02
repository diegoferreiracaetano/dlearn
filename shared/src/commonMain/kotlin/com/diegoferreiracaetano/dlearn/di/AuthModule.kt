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


        factory { SocialSignInUseCase(get(), get()) }

        single { ChallengeCoordinator(get()) }
        single { ChallengeEngine(get(), getAll()) }

        single { OtpChallengeHandler() }
        single<ChallengeHandler> { get<OtpChallengeHandler>() }

        single<ChallengeRepository> { ChallengeRepositoryRemote(get(), get(), get()) }
    }
