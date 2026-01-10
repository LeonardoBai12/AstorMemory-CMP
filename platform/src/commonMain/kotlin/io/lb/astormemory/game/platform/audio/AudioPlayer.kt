package io.lb.astormemory.game.platform.audio

interface AudioPlayer {
    fun playSound(fileName: String, volume: Float = 1.0f)
    fun release()
}
