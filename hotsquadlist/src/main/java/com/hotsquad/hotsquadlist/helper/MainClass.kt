package com.hotsquad.hotsquadlist.helper

import android.app.Application
import android.content.ContextWrapper
import com.hotsquad.hotsquadlist.koin.module.NetworkModule
import com.hotsquad.hotsquadlist.koin.module.RepositoryModule
import com.hotsquad.hotsquadlist.koin.module.ViewModelModule
import com.hotsquad.hotsquadlist.storage.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainClass: Application()
{
    private val appModules = listOf(
        NetworkModule,
        RepositoryModule,
        ViewModelModule
    )

    override fun onCreate() {
        super.onCreate()

        /**
         * Initialize or Start Koin
         */
        startKoin {
            androidContext(this@MainClass)
            modules(appModules)
        }

        /**
         * Initialize the Prefs class
         */
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}