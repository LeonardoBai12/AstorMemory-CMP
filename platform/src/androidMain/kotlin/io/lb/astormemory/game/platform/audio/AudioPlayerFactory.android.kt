@file:OptIn(InternalResourceApi::class)

package io.lb.astormemory.game.platform.audio

import android.content.Context
import org.jetbrains.compose.resources.InternalResourceApi

actual class AudioPlayerFactory(
    private val context: Context
) {
    actual fun create(): AudioPlayer {
        return AndroidAudioPlayer(context)
    }
}