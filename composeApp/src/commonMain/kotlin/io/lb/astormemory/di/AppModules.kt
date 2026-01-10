package io.lb.astormemory.di

import io.lb.astormemory.database.di.databaseModule
import io.lb.astormemory.game.data.di.dataModule
import io.lb.astormemory.game.domain.di.domainModule
import io.lb.astormemory.game.platform.di.commonModule
import io.lb.astormemory.game.platform.di.platformModule

val appModules = listOf(
    commonModule,
    platformModule,
    databaseModule,
    dataModule,
    domainModule,
    viewModelModule,
)
