package io.lb.astormemory.game.platform.music

import android.content.Context
import android.media.MediaPlayer
import io.lb.astormemory.game.platform.R

class AndroidMusicPlayer(
    private val context: Context,
    private val fileName: String
) : MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var wasPaused = false

    override val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying ?: false

    override fun play(volume: Float) {
        if (mediaPlayer == null) {
            val resourceId = getMusicResourceId(fileName)

            mediaPlayer = MediaPlayer.create(context, resourceId).apply {
                isLooping = true
                setVolume(volume, volume)
            }
        }

        if (!isPlaying) {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            wasPaused = false
        }
    }

    override fun pause() {
        if (isPlaying) {
            mediaPlayer?.pause()
            wasPaused = true
        }
    }

    override fun resume() {
        if (wasPaused && !isPlaying) {
            mediaPlayer?.start()
            wasPaused = false
        }
    }

    override fun stop() {
        if (isPlaying) {
            mediaPlayer?.stop()
            wasPaused = false
        }
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        wasPaused = false
    }

    private fun getMusicResourceId(fileName: String): Int {
        return when (fileName) {
            "title_screen.mp3" -> R.raw.title_screen
            "wild.mp3" -> R.raw.wild
            "trainer.mp3" -> R.raw.trainer
            "gym_leader.mp3" -> R.raw.gym_leader
            "final_battle.mp3" -> R.raw.final_battle
            "highscores_screen.mp3" -> R.raw.highscores_screen
            "gameover_screen.mp3" -> R.raw.gameover_screen
            "victory_road.mp3" -> R.raw.victory_road
            "lavender.mp3" -> R.raw.lavender
            "victory_final.mp3" -> R.raw.victory_final
            else -> throw IllegalArgumentException("Unknown music file: $fileName")
        }
    }
}
