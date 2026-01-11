package io.lb.astormemory.game.platform.music

actual class MusicPlayerFactory {
    actual fun createMusicPlayer(fileName: String): MusicPlayer {
        return IosMusicPlayer(fileName)
    }
}