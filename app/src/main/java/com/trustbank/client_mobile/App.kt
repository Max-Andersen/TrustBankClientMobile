package com.trustbank.client_mobile

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.trustbank.client_mobile.di.appModule
import com.trustbank.client_mobile.proto.LocalClientPreferences
import com.trustbank.client_mobile.serializer.LocalClientDataSerializer
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


private const val LOCAL_CLIENT_DATA_STORE_NAME = "client_data.pb"


val Context.localClientDataPreferences: DataStore<LocalClientPreferences> by dataStore(
    fileName = LOCAL_CLIENT_DATA_STORE_NAME,
    serializer = LocalClientDataSerializer(),
)