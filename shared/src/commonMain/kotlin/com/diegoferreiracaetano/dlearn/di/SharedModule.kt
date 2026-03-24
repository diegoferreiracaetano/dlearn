package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.auth.network.AuthInterceptor
import com.diegoferreiracaetano.dlearn.auth.network.ChallengeInterceptor
import com.diegoferreiracaetano.dlearn.data.app.PreferencesRepositoryImpl
import com.diegoferreiracaetano.dlearn.data.app.remote.AppRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.home.remote.HomeRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.main.remote.MainRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.movie.remote.MovieDetailRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.password.remote.PasswordRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.profile.remote.ProfileRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.search.remote.SearchRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import com.diegoferreiracaetano.dlearn.data.source.local.SettingsKeyValueStorage
import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserNetworkDataSource
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.getPlatform
import com.diegoferreiracaetano.dlearn.network.AppUserAgentProvider
import com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.VerifyAccountViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.main.MainViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.movie.MovieDetailViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.search.SearchContentViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.search.SearchMainViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    includes(authModule, platformAuthModule)

    single { GlobalEventDispatcher() }

    single { AppUserAgentProvider(getPlatform(), get()) }
    
    single { 
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            encodeDefaults = true
        }
    }

    single {
        val userAgentProvider = get<AppUserAgentProvider>()
        
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
            
            install(ChallengeInterceptor) {
                engine = get()
                json = get()
            }
        }.apply {
            val authInterceptor = AuthInterceptor(get(), this)

            plugin(HttpSend).intercept { request ->
                authInterceptor.intercept(request) // Injeta Token JWT
                
                val agent = userAgentProvider.get()
                request.header(HttpHeaders.UserAgent, agent.toHeader())
                
                val call = execute(request)
                
                // Se der 401, tenta o refresh e reexecuta se necessário
                if (authInterceptor.handleUnauthorized(call.response)) {
                    authInterceptor.intercept(request)
                    execute(request)
                } else {
                    call
                }
            }
        }
    }

    single { UserNetworkDataSource() }
    single<UserRepository> { UserRepositoryRemote(get()) }
    single { Settings() }
    single<KeyValueStorage> { SettingsKeyValueStorage(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    single<PasswordRepository> { PasswordRepositoryRemote(get()) }
    single<HomeRepository> { HomeRepositoryRemote(get()) }
    single<ProfileRepository> { ProfileRepositoryRemote(get()) }
    single<MovieDetailRepository> { MovieDetailRepositoryRemote(get()) }
    single<MainRepository> { MainRepositoryRemote(get()) }
    single<SearchRepository> { SearchRepositoryRemote(get()) }
    single<AppRepository> { 
        AppRepositoryRemote(
            httpClient = get(), 
            baseUrl = "http://192.168.15.3:8081",
            userAgentProvider = get(),
            preferencesRepository = get(),
            sessionManager = get()
        ) 
    }

    // ViewModels (Shared)
    factory { _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.main.MainViewModel(get()) }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel(
            get(),
            get()
        )
    }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.settings.SettingsViewModel(
            get(),
            get()
        )
    }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.search.SearchMainViewModel(
            get()
        )
    }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.search.SearchContentViewModel(
            get()
        )
    }
    factory { (movieId: String) ->
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.movie.MovieDetailViewModel(
            movieId,
            get()
        )
    }
    single {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel(
            get()
        )
    }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.VerifyAccountViewModel(
            get()
        )
    }
    factory {
        _root_ide_package_.com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel(
            get(),
            get()
        )
    }
}
