package io.lb.astormemory.database.di

import io.lb.astormemory.comon.data.service.DatabaseService
import io.lb.astormemory.database.AstorMemoryDatabase
import io.lb.astormemory.database.factory.DatabaseDriverFactory
import io.lb.astormemory.database.service.DatabaseServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    single { get<DatabaseDriverFactory>().create() }
    single { AstorMemoryDatabase(get()) }
    single<DatabaseService> { DatabaseServiceImpl(get()) }
}

expect val platformDatabaseModule: Module