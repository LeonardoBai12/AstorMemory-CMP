@file:OptIn(ExperimentalForeignApi::class)

package io.lb.astormemory.game.platform.di

import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.music.MusicPlayerFactory
import io.lb.astormemory.game.platform.preferences.SettingsDataStoreFactory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module

actual val platformModule = module {
    single { SettingsDataStoreFactory() }
    single { AudioPlayerFactory() }
    single { MusicPlayerFactory() }
}
