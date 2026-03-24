package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.auth.JsAccountProvider
import org.koin.dsl.module

actual val platformAuthModule = module {
    single<AccountProvider> { JsAccountProvider() }
}
