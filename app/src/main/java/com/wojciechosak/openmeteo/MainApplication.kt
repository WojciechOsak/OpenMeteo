package com.wojciechosak.openmeteo

import android.app.Application
import com.wojciechosak.openmeteo.di.appModule
import com.wojciechosak.openmeteo.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule, networkModule)
        }
    }
}