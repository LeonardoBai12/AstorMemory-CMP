package io.lb.astormemory.game.platform.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.audio.AudioPlayerFactory
import io.lb.astormemory.game.platform.music.MusicPlayerFactory
import io.lb.astormemory.game.platform.preferences.SettingsDataStoreFactory
import io.lb.astormemory.game.platform.utils.MusicManager
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single<DataStore<Preferences>> {
        get<SettingsDataStoreFactory>().create()
    }
    single<AudioPlayer> {
        get<AudioPlayerFactory>().create()
    }
    single {
        MusicManager(get<MusicPlayerFactory>()::createMusicPlayer)
    }
}
