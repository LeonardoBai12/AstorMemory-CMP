package io.lb.astormemory.di

import io.lb.astormemory.app.AppViewModel
import io.lb.astormemory.game.GameViewModel
import io.lb.astormemory.gameover.GameOverViewModel
import io.lb.astormemory.highscore.ScoreViewModel
import io.lb.astormemory.menu.MenuViewModel
import io.lb.astormemory.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { params ->
        GameViewModel(
            useCases = get(),
            sfxAndMusicRepository = get(),
            amount = params.get()
        )
    }
    viewModel {
        ScoreViewModel(
            useCases = get(),
            sfxAndMusicRepository = get()
        )
    }
    viewModel {
        MenuViewModel(
            sfxAndMusicRepository = get(),
            settingsRepository = get()
        )
    }
    viewModel { params ->
        GameOverViewModel(
            sfxAndMusicRepository = get(),
            score = params[0],
            amount = params[1]
        )
    }
    viewModel {
        SettingsViewModel(
            sfxAndMusicRepository = get(),
            settingsRepository = get()
        )
    }
    viewModel { params ->
        AppViewModel(
            settingsRepository = get(),
            sfxAndMusicRepository = get(),
            isSystemInDarkMode = params.get()
        )
    }
}
