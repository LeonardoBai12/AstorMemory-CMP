package io.lb.astormemory.game.domain.di

import io.lb.astormemory.game.domain.usecase.CalculateScoreUseCase
import io.lb.astormemory.game.domain.usecase.GetAstorPairsUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresByAmountUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresUseCase
import io.lb.astormemory.game.domain.usecase.MemoryGameUseCases
import io.lb.astormemory.game.domain.usecase.SaveScoreUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetScoresUseCase(get()) }
    factory { GetScoresByAmountUseCase(get()) }
    factory { SaveScoreUseCase(get()) }
    factory { GetAstorPairsUseCase(get()) }
    factory { CalculateScoreUseCase() }
    
    factory {
        MemoryGameUseCases(
            getScoresUseCase = get(),
            getScoresByAmountUseCase = get(),
            saveScoreUseCase = get(),
            getMemoryGameUseCase = get(),
            calculateScoreUseCase = get()
        )
    }
}
