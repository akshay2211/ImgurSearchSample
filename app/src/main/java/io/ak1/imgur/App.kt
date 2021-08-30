package io.ak1.imgur

import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import io.ak1.imgur.di.databaseModule
import io.ak1.imgur.di.networkModule
import io.ak1.imgur.di.repoModule
import io.ak1.imgur.di.viewModelModule
import io.ak1.imgur.ui.utils.setupTheme
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

        PreferenceManager.getDefaultSharedPreferences(this).setupTheme("list_theme", resources)
    }
}