package com.interview.project

import androidx.multidex.MultiDexApplication
import com.interview.project.di.databaseModule
import com.interview.project.di.networkModule
import com.interview.project.di.repoModule
import com.interview.project.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            //inject Android context
            androidContext(applicationContext)
            // use Android logger - Level.INFO by default
            androidLogger()
            koin.loadModules(listOf(databaseModule, networkModule, viewModelModule, repoModule))
            koin.createRootScope()
        }
    }
}