package io.lb.astormemory.game.platform.music

import android.content.Context

actual class MusicPlayerFactory(private val context: Context) {
    actual fun createMusicPlayer(fileName: String): MusicPlayer {
        return AndroidMusicPlayer(context, fileName)
    }
}
