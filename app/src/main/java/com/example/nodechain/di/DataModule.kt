package com.example.nodechain.di

import com.example.nodechain.data.sharedprefs.SharedPrefs
import com.example.nodechain.data.sharedprefs.SharedPrefsImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module{

    single<SharedPrefs> {
        SharedPrefsImpl(androidContext())
    }

    factory { Gson() }
}