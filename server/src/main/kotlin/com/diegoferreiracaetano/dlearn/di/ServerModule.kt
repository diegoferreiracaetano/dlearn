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
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    single { I18nProvider() }
    
    // Home
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper(get()) }
    single { HomeScreenBuilder(get(), get()) }
    single { HomeOrchestrator(get(), get()) }

    // Profile
    single { ProfileDataService() }
    single { GetProfileDataUseCase(get()) }
    single { ProfileMapper(get()) }
    single { ProfileScreenBuilder(get(), get()) }
    single { ProfileOrchestrator(get(), get()) }
}
