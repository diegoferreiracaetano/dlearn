package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(appModule + sharedModule)
    }
}

val appModule = module {
    factory { HomeViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { (movieId: String) -> MovieDetailViewModel(movieId, get()) }
}
