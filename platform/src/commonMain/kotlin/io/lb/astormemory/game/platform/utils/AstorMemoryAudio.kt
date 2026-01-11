package io.lb.astormemory.game.platform.utils

import io.lb.astormemory.game.platform.audio.AudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val MATCH_VOLUME = 0.7f
private const val FLIP_VOLUME = 0.95f
private const val PLAY_MATCH_DIVIDER = 2f

object AstorMemoryAudio {
    fun playClickEffect(isMuted: Boolean, audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            val volume = if (isMuted) 0.5f else 1.0f
            audioPlayer.playSound("click", volume)
        }
    }

    fun playOptionSelectEffect(isMuted: Boolean, audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            val volume = if (isMuted) 0.1f else 0.4f
            audioPlayer.playSound("option_selected", volume)
        }
    }

    fun playShuffleEffect(isMuted: Boolean, audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            val volume = if (isMuted) 0.4f else 0.65f
            audioPlayer.playSound("shuffle", volume)
        }
    }

    fun stopShuffleEffect(audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            audioPlayer.stopSound("shuffle")
        }
    }

    fun playFlipEffect(isMuted: Boolean, audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            val volume = if (isMuted) FLIP_VOLUME / 2 else FLIP_VOLUME
            audioPlayer.playSound("flip_card_final", volume)
        }
    }
    
    fun playMatchEffect(matches: Int, totalAmount: Int, isMuted: Boolean, audioPlayer: AudioPlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            val isHighMatch = matches > totalAmount * 3 / 4
            val soundFile = if (isHighMatch) "match_effect" else "match_card_init"
            val baseVolume = if (isHighMatch) MATCH_VOLUME / PLAY_MATCH_DIVIDER else MATCH_VOLUME
            val volume = if (isMuted) baseVolume / 2 else baseVolume
            
            audioPlayer.playSound(soundFile, volume)
        }
    }
}
