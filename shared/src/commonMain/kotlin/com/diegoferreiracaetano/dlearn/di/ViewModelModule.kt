package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.VerifyAccountViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.logout.LogoutViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.main.MainViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.movie.MovieDetailViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.CleanCacheViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get(), get()) }
    factory { AppViewModel(get()) }
    factory { SettingsViewModel(get(), get()) }
    factory { (movieId: String) -> MovieDetailViewModel(movieId, get()) }
    factory { CreateNewPasswordViewModel(get()) }
    factory { VerifyAccountViewModel(get()) }
    factory { LoginViewModel(get(), get(), get()) }
    factory { SignUpViewModel(get(), get()) }
    factory { LogoutViewModel(get()) }
    factory { CleanCacheViewModel(get(), get()) }
}
