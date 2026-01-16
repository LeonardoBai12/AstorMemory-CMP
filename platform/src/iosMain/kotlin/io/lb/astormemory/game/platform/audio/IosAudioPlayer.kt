package io.lb.astormemory.game.platform.audio

import astormemory.platform.generated.resources.Res
import io.lb.astormemory.game.platform.audio.AudioPlayer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryAmbient
import platform.AVFAudio.setActive
import platform.Foundation.NSBundle
import platform.Foundation.NSURL

@ExperimentalForeignApi
class IosAudioPlayer : AudioPlayer {

    private val flipCardPlayer: AVAudioPlayer
    private val matchEffectPlayer: AVAudioPlayer
    private val flipInitialCardsPlayer: AVAudioPlayer
    private val shuffleSoundPlayer: AVAudioPlayer
    private val clickSoundPlayer: AVAudioPlayer
    private val optionSelectedPlayer: AVAudioPlayer

    init {
        val audioSession = AVAudioSession.sharedInstance()
        audioSession.setCategory(
            category = AVAudioSessionCategoryAmbient,
            error = null
        )
        audioSession.setActive(true, null)
        val bundle = NSBundle.mainBundle
        flipCardPlayer = createPlayer(bundle, "flip_card_final", "mp3")
        matchEffectPlayer = createPlayer(bundle, "match_effect", "mp3")
        flipInitialCardsPlayer = createPlayer(bundle, "match_card_init", "mp3")
        shuffleSoundPlayer = createPlayer(bundle, "shuffle", "mp3")
        clickSoundPlayer = createPlayer(bundle, "click", "mp3")
        optionSelectedPlayer = createPlayer(bundle, "option_selected", "mp3")
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
        val player = getPlayer(fileName)

        player.volume = volume
        player.currentTime = 0.0
        player.play()
    }

    override fun stopSound(fileName: String) {
        val player = getPlayer(fileName)
        player.stop()
    }

    private fun getPlayer(fileName: String): AVAudioPlayer = when (fileName) {
        "flip_card_final" -> flipCardPlayer
        "match_effect" -> matchEffectPlayer
        "match_card_init" -> flipInitialCardsPlayer
        "shuffle" -> shuffleSoundPlayer
        "click" -> clickSoundPlayer
        "option_selected" -> optionSelectedPlayer
        else -> throw IllegalArgumentException("Sound file not found: $fileName")
    }

    override fun release() {
        flipCardPlayer.stop()
        matchEffectPlayer.stop()
        flipInitialCardsPlayer.stop()
    }
}