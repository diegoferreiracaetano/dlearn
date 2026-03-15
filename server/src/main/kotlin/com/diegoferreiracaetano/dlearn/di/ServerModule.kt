package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.usecases.*
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.WatchProviderUrlMapper
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.services.*
import com.diegoferreiracaetano.dlearn.orchestrator.*
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.screens.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    single { I18nProvider() }
    single { WatchProviderUrlMapper() }
    single { TmdbMapper(get()) }
    
    // Home
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper(get()) }
    single { HomeScreenBuilder(get(), get()) }
    single { HomeOrchestrator(get(), get()) }

    // Main
    single { NewScreenBuilder(get(), get()) }
    single { FavoriteScreenBuilder(get(), get()) }
    single { MainScreenBuilder(get()) }
    single { MainOrchestrator(get(), get(), get(), get(), get(), get(), get()) }

    // Profile
    single { ProfileDataService() }
    single { GetProfileDataUseCase(get()) }
    single { ProfileMapper(get()) }
    single { ProfileScreenBuilder(get(), get()) }
    single { ProfileOrchestrator(get(), get()) }

    // Movie Detail
    single { MovieDetailDataService(get(), get()) }
    single { GetMovieDetailUseCase(get()) }
    single { MovieDetailMapper(get()) }
    single { MovieDetailScreenBuilder(get(), get()) }
    single { MovieDetailOrchestrator(get(), get()) }

    // Search
    single { SearchDataService(get()) }
    single { GetSearchDataUseCase(get()) }
    single { SearchScreenBuilder(get()) }
    single { SearchOrchestrator(get(), get()) }
}
