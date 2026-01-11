package io.lb.astormemory.game.platform.music

expect class MusicPlayerFactory {
    fun createMusicPlayer(fileName: String): MusicPlayer
}
