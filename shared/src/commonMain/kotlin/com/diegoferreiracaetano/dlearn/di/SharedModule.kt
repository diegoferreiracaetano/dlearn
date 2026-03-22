package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.auth.network.ChallengeInterceptor
import com.diegoferreiracaetano.dlearn.data.app.remote.AppRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.home.remote.HomeRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.main.remote.MainRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.movie.remote.MovieDetailRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.password.remote.PasswordRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.profile.remote.ProfileRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.search.remote.SearchRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.session.SessionStorage
import com.diegoferreiracaetano.dlearn.data.session.SettingsSessionStorage
import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserNetworkDataSource
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Módulo principal compartilhado que integra os sub-módulos específicos.
 */
val sharedModule = module {
    // Importa o módulo de Auth isolado
    includes(authModule)

    // Componentes de Infraestrutura e Core
    single { GlobalEventDispatcher() }
    
    single { 
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    // Configuração do Cliente HTTP com Interceptor de Auth
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get<Json>())
            }
            install(Logging) {
                level = LogLevel.ALL
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "192.168.15.3"
                    port = 8081
                }
            }
            
            // O interceptor agora usa o engine provido pelo authModule
            install(ChallengeInterceptor) {
                engine = get()
                json = get()
            }
        }
    }

    // Session e User
    single { UserNetworkDataSource() }
    single<UserRepository> { UserRepositoryRemote(get()) }
    single { Settings() }
    single<SessionStorage> { SettingsSessionStorage(get()) }
    single { SessionManager(get()) }

    // Repositórios de Domínio
    single<PasswordRepository> { PasswordRepositoryRemote(get()) }
    single<HomeRepository> { HomeRepositoryRemote(get()) }
    single<ProfileRepository> { ProfileRepositoryRemote(get()) }
    single<MovieDetailRepository> { MovieDetailRepositoryRemote(get()) }
    single<MainRepository> { MainRepositoryRemote(get()) }
    single<SearchRepository> { SearchRepositoryRemote(get()) }
    single<AppRepository> { AppRepositoryRemote(get()) }
}
