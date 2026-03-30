package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.api.exception.ChallengeMapper
import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.*
import com.diegoferreiracaetano.dlearn.domain.usecases.auth.LinkExternalProviderUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import com.diegoferreiracaetano.dlearn.infrastructure.auth.ExternalAuthService
import com.diegoferreiracaetano.dlearn.infrastructure.auth.TmdbAuthService
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCacheManager
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.WatchProviderUrlMapper
import com.diegoferreiracaetano.dlearn.infrastructure.services.*
import com.diegoferreiracaetano.dlearn.orchestrator.app.*
import com.diegoferreiracaetano.dlearn.orchestrator.auth.CreateUserOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.LoginOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.PasswordOrchestrator
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.*
import com.diegoferreiracaetano.dlearn.ui.screens.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes

val serverModule = module {
    // 1. Infraestrutura Base
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
        }
    }
    
    single<CacheManager> { InMemoryCacheManager(expiration = 10.minutes) }
    single { I18nProvider() }
    single<FeatureToggleService> { InMemoryFeatureToggleService() }

    // 2. Clientes de Terceiros e Mapeadores de Infra
    single { TmdbClient(get()) }
    single { TmdbMapper(get()) }
    single { WatchProviderUrlMapper() }
    single { VideoMapper() }
    
    // 3. Repositórios (Data Sources)
    single<AuthProviderRepository> { AuthProviderDataService() }
    single<UserRepository> { UserDataService() }
    single<FavoriteRepository> { FavoriteDataService() }
    single<WatchlistRepository> { WatchlistDataService(get(), get()) }

    // 4. Serviços de Domínio e Lógica de Negócio
    single { HomeDataService(get(), get()) }
    single { MovieDetailDataService(get(), get(), get()) }
    single { SearchDataService(get()) }
    single { FaqDataService() }
    single { TokenService() }
    single { ChallengeDataService() }
    single { PasswordDataService(get(), get()) }
    
    // External Auth Services
    single { TmdbAuthService(get()) }
    single<ExternalAuthService> { get<TmdbAuthService>() }
    
    single { AuthProviderSyncService(getAll<ExternalAuthService>(), get(), get()) }
    
    single { ChallengeMapper() }

    // 5. Casos de Uso
    single { GetHomeDataUseCase(get()) }
    single { GetMovieDetailUseCase(get()) }
    single { GetSearchDataUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { LinkExternalProviderUseCase(get()) }

    // 6. Mapeadores de UI (SDUI)
    single { HomeMapper() }
    single { MovieDetailMapper(get()) }
    single { ProfileMapper(get()) }
    single { SettingsMapper(get()) }
    single { VideoMapper() }

    // 7. Screen Builders (BFF)
    single { HomeScreenBuilder(get(), get()) }
    single { MovieDetailScreenBuilder(get()) }
    single { WatchlistScreenBuilder(get()) }
    single { FavoriteScreenBuilder(get()) }
    single { MainScreenBuilder(get()) }
    single { ProfileScreenBuilder(get()) }
    single { EditProfileScreenBuilder(get(), get()) }
    single { SettingsScreenBuilder(get(), get()) }
    single { SearchScreenBuilder(get()) }
    single { FaqScreenBuilder() }
    single { VerifyAccountScreenBuilder() }
    single { UserListScreenBuilder() }

    // 8. Orchestrators (Gateways de Tela)
    single { HomeOrchestrator(get(), get()) }
    single { MovieDetailOrchestrator(get(), get(), get(), get()) }
    single { LoginOrchestrator(get(), get(), get()) }
    single { CreateUserOrchestrator(get(), get(), get()) }
    single { PasswordOrchestrator(get()) }
    single { WatchlistOrchestrator(get(), get(), get(), get()) }
    single { FavoriteOrchestrator(get(), get(), get(), get()) }
    single { SearchOrchestrator(get(), get(), get(), get()) }
    single { ProfileOrchestrator(get(), get(), get()) }
    single { MainOrchestrator(get()) }
    single { FaqOrchestrator(get(), get()) }
    single { VerifyAccountOrchestrator(get()) }
    single { SettingsOrchestrator(get()) }
    single { UserListOrchestrator(get(), get()) }
    
    // 9. AppOrchestrator (Main Gateway)
    single<Orchestrator> {
        AppOrchestrator(
            get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get()
        )
    }
}
