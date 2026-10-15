package io.lb.astormemory

import androidx.compose.ui.window.ComposeUIViewController
import io.lb.astormemory.app.AstorMemoryApp
import io.lb.astormemory.database.di.platformDatabaseModule
import io.lb.astormemory.di.appModules
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.core.context.startKoin
import platform.posix.exit

private var isKoinInitialized = false

@InternalResourceApi
fun MainViewController() = ComposeUIViewController {
    if (!isKoinInitialized) {
        isKoinInitialized = true
        startKoin {
            modules(appModules + platformDatabaseModule)
        }
    }
    AstorMemoryApp(
        onQuitApp = {
            exit(0)
        }
    )
}
