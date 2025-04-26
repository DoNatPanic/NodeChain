package com.example.nodechain.di

import com.example.nodechain.data.chain.NodeRepositoryImpl
import com.example.nodechain.data.sharedprefs.SharedPrefs
import com.example.nodechain.data.sharedprefs.SharedPrefsImpl
import com.example.nodechain.domain.chain.NodeRepository
import com.example.nodechain.ui.chain.viewmodel.ChainViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {

    single<SharedPrefs> {
        SharedPrefsImpl(androidContext())
    }

    factory { Gson() }

    factory<NodeRepository> {
        NodeRepositoryImpl(get(), get())
    }

    viewModel {
        ChainViewModel(get())
    }
}