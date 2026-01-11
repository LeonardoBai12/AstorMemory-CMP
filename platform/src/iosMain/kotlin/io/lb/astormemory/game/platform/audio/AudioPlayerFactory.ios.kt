package io.lb.astormemory.game.platform.audio

import kotlinx.cinterop.ExperimentalForeignApi

@ExperimentalForeignApi
actual class AudioPlayerFactory {
    actual fun create(): AudioPlayer {
        return IosAudioPlayer()
    }
}
