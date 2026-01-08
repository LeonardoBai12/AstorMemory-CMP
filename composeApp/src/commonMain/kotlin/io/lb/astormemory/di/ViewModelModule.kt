package io.lb.astormemory.di

import io.lb.astormemory.game.GameViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { params ->
        GameViewModel(
            useCases = get(),
            amount = params.get()
        )
    }
}
