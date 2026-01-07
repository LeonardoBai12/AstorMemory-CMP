package io.lb.astormemory

import androidx.compose.ui.window.ComposeUIViewController
import io.lb.astormemory.database.di.platformDatabaseModule
import io.lb.astormemory.di.appModules
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.core.context.startKoin

@InternalResourceApi
fun MainViewController() = ComposeUIViewController {
    startKoin {
        modules(appModules + platformDatabaseModule)
    }
    App()
}
