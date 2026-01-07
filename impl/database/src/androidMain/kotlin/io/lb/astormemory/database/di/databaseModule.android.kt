package io.lb.astormemory.database.di

import io.lb.astormemory.database.factory.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single { DatabaseDriverFactory(androidContext()) }
}