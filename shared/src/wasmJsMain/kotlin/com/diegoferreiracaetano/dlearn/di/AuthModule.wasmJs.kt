package com.diegoferreiracaetano.dlearn.di

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.auth.WasmJsAccountProvider
import org.koin.dsl.module

actual val platformAuthModule = module {
    single<AccountProvider> { WasmJsAccountProvider() }
}
