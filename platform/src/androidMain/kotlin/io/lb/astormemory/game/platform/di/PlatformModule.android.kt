package io.lb.astormemory.game.platform.di

import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.music.MusicPlayerFactory
import io.lb.astormemory.game.platform.preferences.PreferencesFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { PreferencesFactory(androidContext()) }
    single { AudioPlayerFactory(androidContext()) }
    single { MusicPlayerFactory(androidContext()) }
}
