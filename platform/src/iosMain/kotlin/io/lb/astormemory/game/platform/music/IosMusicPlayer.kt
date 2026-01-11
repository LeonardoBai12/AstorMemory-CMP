package io.lb.astormemory.game.platform.music

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
class IosMusicPlayer(private val fileName: String) : MusicPlayer {
    private var player: AVAudioPlayer? = null
    private var wasPaused = false
    
    override val isPlaying: Boolean
        get() = player?.playing ?: false
    
    override fun play(volume: Float) {
        if (player == null) {
            val bundle = NSBundle.mainBundle
            val resourceName = fileName.substringBeforeLast(".")
            val extension = fileName.substringAfterLast(".")
            
            val path = bundle.pathForResource(resourceName, extension)
                ?: throw IllegalArgumentException("Music file not found: $fileName")
            
            val url = NSURL.fileURLWithPath(path)
            player = AVAudioPlayer(url, error = null).apply {
                numberOfLoops = -1
                this.volume = volume
                prepareToPlay()
            }
        }
        
        if (!isPlaying) {
            player?.currentTime = 0.0
            player?.play()
            wasPaused = false
        }
    }
    
    override fun pause() {
        if (isPlaying) {
            player?.pause()
            wasPaused = true
        }
    }
    
    override fun resume() {
        if (wasPaused && !isPlaying) {
            player?.play()
            wasPaused = false
        }
    }
    
    override fun stop() {
        if (isPlaying) {
            player?.stop()
            wasPaused = false
        }
    }
    
    override fun release() {
        player?.stop()
        player = null
        wasPaused = false
    }
}
