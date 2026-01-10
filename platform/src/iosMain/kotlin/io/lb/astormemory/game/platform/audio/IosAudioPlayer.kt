package io.lb.astormemory.game.platform.audio

import astormemory.platform.generated.resources.Res
import io.lb.astormemory.game.platform.audio.AudioPlayer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL

@ExperimentalForeignApi
class IosAudioPlayer : AudioPlayer {

    private val loadedUrls = mutableMapOf<String, NSURL>()

    override fun playSound(fileName: String, volume: Float) {
        val url = loadedUrls.getOrPut(fileName) {
            val uri = Res.getUri("files/$fileName")
            NSURL.URLWithString(URLString = uri) ?: throw IllegalArgumentException("Invalid URL: $uri")
        }
        val player = runCatching {
            AVAudioPlayer(url, error = null)
        }.getOrElse {
            throw IllegalArgumentException("Could not create AVAudioPlayer for URL: $url")
        }
        player.volume = volume
        player.prepareToPlay()
        player.play()
    }

    override fun release() {
        loadedUrls.clear()
    }
}