package com.trustbank.client_mobile

import android.app.Application
import com.trustbank.client_mobile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        super.onCreate()
    }
}