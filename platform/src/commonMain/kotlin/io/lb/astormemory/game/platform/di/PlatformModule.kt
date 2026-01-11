package io.lb.astormemory.game.platform.di

import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.preferences.PreferencesFactory
import io.lb.astormemory.game.platform.utils.MusicManager
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single<AppPreferences> {
        get<PreferencesFactory>().create()
    }
    single<AudioPlayer> {
        get<AudioPlayerFactory>().create()
    }
    single {
        MusicManager(get())
    }
}
