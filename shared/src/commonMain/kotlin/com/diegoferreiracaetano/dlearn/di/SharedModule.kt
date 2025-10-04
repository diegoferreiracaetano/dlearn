package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.session.SessionStorage
import com.diegoferreiracaetano.dlearn.data.session.SettingsSessionStorage
import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserNetworkDataSource
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.CreateAccountUseCase
import com.diegoferreiracaetano.dlearn.domain.user.LoginUseCase
import com.diegoferreiracaetano.dlearn.domain.user.SendCodeUseCase
import com.diegoferreiracaetano.dlearn.domain.user.VerifyCodeUseCase
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val sharedModule = module {

    single { UserNetworkDataSource() }
    single<UserRepository> { UserRepositoryRemote(get()) }
    single { CreateAccountUseCase(get()) }
    single { LoginUseCase(get(), get()) }
    single { SendCodeUseCase(get()) }
    single { VerifyCodeUseCase(get()) }
    single { Settings() }
    single<SessionStorage> { SettingsSessionStorage(get()) }
    single { SessionManager(get()) }

//    single { PokemonLocalDataSource() }
//    single<PokemonRepository> { PokemonRepositoryRemote(get()) }
}