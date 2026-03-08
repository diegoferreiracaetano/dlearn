package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService
import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService
import com.diegoferreiracaetano.dlearn.orchestrator.HomeOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.ProfileOrchestrator
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    
    // Home
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper() }
    single { HomeScreenBuilder(get()) }
    single { HomeOrchestrator(get(), get()) }

    // Profile
    single { ProfileDataService() }
    single { GetProfileDataUseCase(get()) }
    single { ProfileMapper() }
    single { ProfileScreenBuilder(get()) }
    single { ProfileOrchestrator(get(), get()) }
}
