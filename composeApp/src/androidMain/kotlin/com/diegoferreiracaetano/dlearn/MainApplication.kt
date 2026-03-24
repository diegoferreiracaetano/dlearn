package com.diegoferreiracaetano.dlearn

import android.app.Application
import com.diegoferreiracaetano.dlearn.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}
