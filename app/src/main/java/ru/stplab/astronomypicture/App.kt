package ru.stplab.astronomypicture

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.stplab.astronomypicture.di.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModelModule, apiModule, netModule, databaseModule, repositoryModule))
        }
    }
}