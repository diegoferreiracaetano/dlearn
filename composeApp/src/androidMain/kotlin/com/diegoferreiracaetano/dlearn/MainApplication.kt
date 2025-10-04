package com.diegoferreiracaetano.dlearn

import android.app.Application
import com.diegoferreiracaetano.dlearn.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}