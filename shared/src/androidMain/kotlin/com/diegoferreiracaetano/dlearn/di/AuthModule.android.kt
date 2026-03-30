package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.auth.AndroidAccountProvider
import com.diegoferreiracaetano.dlearn.domain.auth.AndroidSocialAuthManager
import com.diegoferreiracaetano.dlearn.domain.auth.SocialAuthManager
import org.koin.dsl.module

actual val platformAuthModule = module {
    single<AccountProvider> { AndroidAccountProvider(get()) }
    single<SocialAuthManager> { AndroidSocialAuthManager(get()) }
}
