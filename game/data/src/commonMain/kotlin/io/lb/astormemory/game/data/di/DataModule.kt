package io.lb.astormemory.game.data.di

import io.lb.astormemory.game.data.datasource.MemoryGameDataSource
import io.lb.astormemory.game.data.repository.MemoryGameRepositoryImpl
import io.lb.astormemory.game.data.resources.AstorResourceProvider
import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import org.koin.dsl.module

val dataModule = module {
    single { AstorResourceProvider() }
    single<MemoryGameDataSource> { MemoryGameDataSource(get()) }
    single<MemoryGameRepository> { MemoryGameRepositoryImpl(get(), get()) }
}
