@file:OptIn(ExperimentalForeignApi::class)

package io.lb.astormemory.game.platform.di

import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.music.MusicPlayerFactory
import io.lb.astormemory.game.platform.preferences.PreferencesFactory
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module

actual val platformModule = module {
    single { PreferencesFactory() }
    single { AudioPlayerFactory() }
    single { MusicPlayerFactory() }
}
