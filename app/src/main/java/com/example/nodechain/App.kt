package com.example.nodechain

import android.app.Application
import com.example.nodechain.di.dataModule
import com.example.nodechain.di.domainModule
import com.example.nodechain.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Инициализация Koin
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                domainModule,
                viewModelModule
            )
        }
    }
}