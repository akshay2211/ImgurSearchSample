package com.interview.project.di

import androidx.lifecycle.SavedStateHandle
import com.interview.project.data.repository.ImgurPostsRepository
import com.interview.project.ui.main.MainActivityViewHolder
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

var networkModule = module {
    single { getLogInterceptor() }
    single { returnRetrofit(get()) }
    single { getApi(get()) }
}

var databaseModule = module {
    single { getDb(androidApplication()) }
}

var repoModule = module {
    single { getCoroutineContext() }
    single { ImgurPostsRepository(get(), get(), get()) }
}

var viewModelModule = module {
    viewModel { (state: SavedStateHandle) -> MainActivityViewHolder(handle = state, get()) }
}