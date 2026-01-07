package io.lb.astormemory.di

import io.lb.astormemory.database.di.databaseModule
import io.lb.astormemory.game.data.di.dataModule
import io.lb.astormemory.game.domain.di.domainModule

val appModules = listOf(
    databaseModule,
    dataModule,
    domainModule
)
