package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService
import com.diegoferreiracaetano.dlearn.orchestrator.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper() }
    single { HomeScreenBuilder(get()) }
    single { HomeOrchestrator(get(), get()) }
}
