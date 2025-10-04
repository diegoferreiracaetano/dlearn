package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.ui.views.home.HomeViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(appModule + sharedModule)
    }
}

val appModule = module {
    factory { HomeViewModel() }
}