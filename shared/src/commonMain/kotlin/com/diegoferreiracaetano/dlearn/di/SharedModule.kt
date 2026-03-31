package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.AppConstants.X_COUNTRY
import com.diegoferreiracaetano.dlearn.AppConstants.X_NOTIFICATIONS_ENABLED
import com.diegoferreiracaetano.dlearn.auth.network.AuthInterceptor
import com.diegoferreiracaetano.dlearn.auth.network.ChallengeInterceptor
import com.diegoferreiracaetano.dlearn.data.app.PreferencesRepositoryImpl
import com.diegoferreiracaetano.dlearn.data.app.remote.AppRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.auth.remote.AuthRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import com.diegoferreiracaetano.dlearn.data.cache.PersistentCacheManager
import com.diegoferreiracaetano.dlearn.data.home.remote.HomeRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.main.remote.MainRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.movie.remote.MovieDetailRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.password.remote.PasswordRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.profile.remote.ProfileRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import com.diegoferreiracaetano.dlearn.data.source.local.SettingsKeyValueStorage
import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserNetworkDataSource
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserRepositoryRemote
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.auth.AuthRepository
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.domain.password.PasswordRepository
import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.getPlatform
import com.diegoferreiracaetano.dlearn.network.AppUserAgentProvider
import com.diegoferreiracaetano.dlearn.network.error.toAppException
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
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
    includes(authModule, platformAuthModule, viewModelModule)

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
        val preferencesRepository = get<PreferencesRepository>()
        
        HttpClient {
            expectSuccess = true 
            
            install(ContentNegotiation) {
                json(get<Json>())
            }
            install(Logging) {
                level = LogLevel.ALL
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    throw cause.toAppException()
                }
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
                authInterceptor.intercept(request)
                
                // Adicionando headers globais dinâmicos
                val agent = userAgentProvider.get()
                request.header(HttpHeaders.UserAgent, agent.toHeader())
                request.header(HttpHeaders.AcceptLanguage, preferencesRepository.language)
                request.header(X_COUNTRY, preferencesRepository.country)
                request.header(X_NOTIFICATIONS_ENABLED, preferencesRepository.notificationsEnabled.toString())
                
                val call = execute(request)
                
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
    single<PreferencesRepository> { PreferencesRepositoryImpl(get(), getPlatform()) }

    // Novo Gerenciador de Cache (Interface e Implementação persistente para o App)
    single<CacheManager> { PersistentCacheManager(get(), get()) }

    single<PasswordRepository> { PasswordRepositoryRemote(get()) }
    single<HomeRepository> { HomeRepositoryRemote(get()) }
    single<ProfileRepository> { ProfileRepositoryRemote(get()) }
    single<MovieDetailRepository> { MovieDetailRepositoryRemote(get()) }

    single<MainRepository> { MainRepositoryRemote(get()) }
    
    single<AppRepository> { AppRepositoryRemote(get()) }
    
    single<AuthRepository> {
        AuthRepositoryRemote(
            httpClient = get(), sessionManager = get())
    }
}
