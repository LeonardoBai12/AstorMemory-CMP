package io.lb.astormemory.game.platform.music

interface MusicPlayer {
    fun play(volume: Float = 1.0f)
    fun pause()
    fun resume()
    fun stop()
    fun release()
    val isPlaying: Boolean
}
