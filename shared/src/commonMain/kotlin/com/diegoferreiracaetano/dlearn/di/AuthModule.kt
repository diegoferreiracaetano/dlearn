package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import org.koin.dsl.module

val authModule = module {
    single { SessionManager(get()) }
    
    // Challenge Engine & Coordinator
    single { ChallengeCoordinator(get()) }
    single { ChallengeEngine(get(), getAll()) }
    
    // Handlers de Desafio
    single<ChallengeHandler> { OtpChallengeHandler(get()) }
    // O BiometricChallengeHandler será injetado pelo platformAuthModule se disponível

    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel(
            get(),
            get()
        )
    }
}

expect val platformAuthModule: org.koin.core.module.Module
