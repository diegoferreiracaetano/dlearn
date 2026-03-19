package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.screens.app.AppViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.main.MainViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.movie.MovieDetailViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.search.SearchContentViewModel
import com.diegoferreiracaetano.dlearn.ui.screens.search.SearchMainViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(appModule, renderModule, sharedModule)
    }
}

val appModule = module {
    factory { MainViewModel(get()) }
    factory { HomeViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { AppViewModel(get()) }
    single { SearchMainViewModel(get()) }
    single { SearchContentViewModel(get()) }
    factory { (movieId: String) -> MovieDetailViewModel(movieId, get()) }
}
