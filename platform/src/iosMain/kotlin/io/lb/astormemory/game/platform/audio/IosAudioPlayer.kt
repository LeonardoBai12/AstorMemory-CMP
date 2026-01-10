package io.lb.astormemory.game.platform.audio

import astormemory.platform.generated.resources.Res
import io.lb.astormemory.game.platform.audio.AudioPlayer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSURL

@ExperimentalForeignApi
class IosAudioPlayer : AudioPlayer {

    private val flipCardPlayer: AVAudioPlayer
    private val matchEffectPlayer: AVAudioPlayer
    private val flipInitialCardsPlayer: AVAudioPlayer

    init {
        val bundle = NSBundle.mainBundle
        flipCardPlayer = createPlayer(bundle, "flip_card_final", "mp3")
        matchEffectPlayer = createPlayer(bundle, "match_effect", "mp3")
        flipInitialCardsPlayer = createPlayer(bundle, "match_card_init", "mp3")
    }

    private fun createPlayer(bundle: NSBundle, name: String, ext: String): AVAudioPlayer {
        val path = bundle.pathForResource(name, ext)
            ?: throw IllegalArgumentException("Sound file not found: $name.$ext")

        val url = NSURL.fileURLWithPath(path)
        val player = AVAudioPlayer(url, error = null)
            ?: throw IllegalStateException("Failed to create AVAudioPlayer for $name.$ext")

        player.prepareToPlay()
        return player
    }

    override fun playSound(fileName: String, volume: Float) {
        val player = when (fileName) {
            "flip_card_final" -> flipCardPlayer
            "match_effect" -> matchEffectPlayer
            "match_card_init" -> flipInitialCardsPlayer
            else -> throw IllegalArgumentException("Sound file not found: $fileName")
        }

        player.volume = volume
        player.currentTime = 0.0
        player.play()
    }

    override fun release() {
        flipCardPlayer.stop()
        matchEffectPlayer.stop()
        flipInitialCardsPlayer.stop()
    }
}