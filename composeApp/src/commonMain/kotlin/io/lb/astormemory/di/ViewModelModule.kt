package io.lb.astormemory.di

import io.lb.astormemory.game.GameViewModel
import io.lb.astormemory.highscore.ScoreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { params ->
        GameViewModel(
            useCases = get(),
            audioPlayer = get(),
            prefs = get(),
            amount = params.get()
        )
    }
    viewModel {
        ScoreViewModel(
            useCases = get()
        )
    }
}
