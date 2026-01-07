package io.lb.astormemory

import android.app.Application
import io.lb.astormemory.database.di.platformDatabaseModule
import io.lb.astormemory.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AstorMemoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AstorMemoryApplication)
            modules(appModules + platformDatabaseModule)
        }
    }
}
