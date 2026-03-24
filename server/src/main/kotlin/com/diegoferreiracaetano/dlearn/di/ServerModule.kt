package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.api.exception.ChallengeMapper
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.*
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.WatchProviderUrlMapper
import com.diegoferreiracaetano.dlearn.infrastructure.services.*
import com.diegoferreiracaetano.dlearn.orchestrator.*
import com.diegoferreiracaetano.dlearn.orchestrator.app.*
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.*
import com.diegoferreiracaetano.dlearn.ui.screens.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import org.koin.dsl.module

val serverModule = module {
    single { TmdbClient() }
    single { I18nProvider() }
    single { WatchProviderUrlMapper() }
    single { TmdbMapper(get()) }
    single { VideoMapper() }
    
    // API / Exception Handling / Auth
    single { ChallengeMapper() }
    single { TokenService() }
    
    // Repositories / Services
    single<FavoriteRepository> { FavoriteDataService() }
    single<WatchlistRepository> { WatchlistDataService() }
    single { FaqDataService() }
    
    // Home
    single { HomeDataService(get()) }
    single { GetHomeDataUseCase(get()) }
    single { HomeMapper(get()) }
    single { HomeScreenBuilder(get(), get()) }
    single { HomeOrchestrator(get(), get()) }

    // Auth & Login (SDUI)
    single { LoginOrchestrator(get()) }

    // Data Services & UseCases (Profile, Settings, Movie, Search)
    single { ProfileDataService() }
    single { GetProfileDataUseCase(get()) }
    single { UpdateProfileDataUseCase(get()) }
    single { ProfileMapper(get()) }
    
    single { SettingsMapper(get()) }
    
    single { MovieDetailDataService(get(), get()) }
    single { GetMovieDetailUseCase(get()) }
    single { MovieDetailMapper(get()) }
    
    single { SearchDataService(get()) }
    single { GetSearchDataUseCase(get()) }

    // Screen Builders
    single { WatchlistScreenBuilder(get()) }
    single { FavoriteScreenBuilder(get()) }
    single { MainScreenBuilder(get()) }
    single { VerifyAccountScreenBuilder() }
    single { FaqScreenBuilder() }
    single { ProfileScreenBuilder(get()) }
    single { EditProfileScreenBuilder(get(), get()) }
    single { SettingsScreenBuilder(get(), get()) }
    single { MovieDetailScreenBuilder(get(), get()) }
    single { SearchScreenBuilder(get()) }

    // Individual Orchestrators
    single { WatchlistOrchestrator(get(), get(), get(), get()) }
    single { FavoriteOrchestrator(get(), get(), get(), get()) }
    single { MainOrchestrator(get()) }
    single { FaqOrchestrator(get(), get()) }
    single { VerifyAccountOrchestrator(get()) }
    single { ProfileOrchestrator(get(), get(), get(), get()) }
    single { SettingsOrchestrator(get()) }
    single { MovieDetailOrchestrator(get(), get()) }
    single { SearchOrchestrator(get(), get(), get(), get()) }
    
    // AppOrchestrator (Main Gateway)
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
            get(),
            get()
        )
    }

    // Challenge Engine
    single { ChallengeDataService() }

    // Password
    single { PasswordDataService(get()) }
    single { ChangePasswordUseCase(get()) }
    single { PasswordOrchestrator(get()) }
}
