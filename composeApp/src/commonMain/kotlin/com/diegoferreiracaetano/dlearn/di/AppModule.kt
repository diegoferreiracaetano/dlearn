package com.diegoferreiracaetano.dlearn.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule, sharedModule)
    }
}

@Suppress("unused")
fun doInitKoin() {
    initKoin()
}

val appModule = module {
    // No longer needed: RenderComponentFactory and individual renderers are now a single static object.
}
