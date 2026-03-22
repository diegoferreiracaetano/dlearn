package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.api.exception.ChallengeMapper
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.*
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.WatchProviderUrlMapper
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.services.*
import com.diegoferreiracaetano.dlearn.orchestrator.*
import com.diegoferreiracaetano.dlearn.orchestrator.app.*
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    single { I18nProvider() }
    single { WatchProviderUrlMapper() }
    single { TmdbMapper(get()) }
    single { VideoMapper() }
    
    // API / Exception Handling
    single { ChallengeMapper() }
    
    // Repositories / Services
    single<FavoriteRepository> { FavoriteDataService() }
    single<WatchlistRepository> { WatchlistDataService() }
    single { FaqDataService() }
    
    // Home
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper(get()) }
    single { HomeScreenBuilder(get(), get()) }
    single<HomeOrchestrator> { HomeOrchestrator(get(), get()) }

    // Main / App SDUI
    single { WatchlistScreenBuilder(get()) }
    single { FavoriteScreenBuilder(get()) }
    single { MainScreenBuilder(get()) }
    single { VerifyAccountScreenBuilder() }
    single { FaqScreenBuilder() }
    single<WatchlistOrchestrator> { WatchlistOrchestrator(get(), get(), get(), get()) }
    single<FavoriteOrchestrator> { FavoriteOrchestrator(get(), get(), get(), get()) }
    single<MainOrchestrator> { MainOrchestrator(get()) }
    single<FaqOrchestrator> { FaqOrchestrator(get(), get()) }
    single { VerifyAccountOrchestrator(get()) }
    single<Orchestrator> {
        AppOrchestrator(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // Profile
    single { ProfileDataService() }
    single { GetProfileDataUseCase(get()) }
    single { UpdateProfileDataUseCase(get()) }
    single { ProfileMapper(get()) }
    single { ProfileScreenBuilder(get(), get()) }
    single { EditProfileScreenBuilder(get(), get()) }
    single<ProfileOrchestrator> { ProfileOrchestrator(get(), get(), get(), get()) }

    // Movie Detail
    single { MovieDetailDataService(get(), get()) }
    single { GetMovieDetailUseCase(get()) }
    single { MovieDetailMapper(get()) }
    single { MovieDetailScreenBuilder(get(), get()) }
    single<MovieDetailOrchestrator> { MovieDetailOrchestrator(get(), get()) }

    // Search
    single { SearchDataService(get()) }
    single { GetSearchDataUseCase(get()) }
    single { SearchScreenBuilder(get()) }
    single<SearchOrchestrator> { SearchOrchestrator(get(), get(), get(), get()) }

    // Challenge Engine
    single { ChallengeDataService() }

    // Password
    single { PasswordDataService(get()) }
    single { ChangePasswordUseCase(get()) }
    single<PasswordOrchestrator> { PasswordOrchestratorImpl(get()) }
}
