package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.data.home.remote.HomeRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.session.SessionStorage
import com.diegoferreiracaetano.dlearn.data.session.SettingsSessionStorage
import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserNetworkDataSource
import com.diegoferreiracaetano.dlearn.data.user.source.remote.UserRepositoryRemote
import com.diegoferreiracaetano.dlearn.data.video.source.remote.VideoNetworkDataSource
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.CreateAccountUseCase
import com.diegoferreiracaetano.dlearn.domain.user.LoginUseCase
import com.diegoferreiracaetano.dlearn.domain.user.SendCodeUseCase
import com.diegoferreiracaetano.dlearn.domain.user.VerifyCodeUseCase
import com.diegoferreiracaetano.dlearn.shared.BuildConfig
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {


    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "192.168.15.4"
                    port = 8081
                }
            }
        }
    }

    single { UserNetworkDataSource() }
    single<UserRepository> { UserRepositoryRemote(get()) }
    single { CreateAccountUseCase(get()) }
    single { LoginUseCase(get(), get()) }
    single { SendCodeUseCase(get()) }
    single { VerifyCodeUseCase(get()) }
    single { Settings() }
    single<SessionStorage> { SettingsSessionStorage(get()) }
    single { SessionManager(get()) }

    single { VideoNetworkDataSource(get(), BuildConfig.THE_MOVIE_DB_API_KEY) }
//    single<VideoRepository> { VideoRepositoryRemote(get()) }
    single<HomeRepository> { HomeRepositoryRemote(get()) }
}
