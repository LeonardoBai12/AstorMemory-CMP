package io.lb.astormemory.game.platform.audio

import android.content.Context
import android.media.SoundPool
import io.lb.astormemory.game.platform.R
import io.lb.astormemory.game.platform.audio.AudioPlayer

class AndroidAudioPlayer(context: Context) : AudioPlayer {
    private var flipCardEffectId = 0
    private var matchEffectId = 0
    private var flipInitialCardsEffectId = 0

    fun buildSoundPool(context: Context): SoundPool {
        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        flipCardEffectId = soundPool.load(context, R.raw.flip_card_final, 1)
        matchEffectId = soundPool.load(context, R.raw.match_effect, 1)
        flipInitialCardsEffectId = soundPool.load(context, R.raw.match_card_init, 1)

        return soundPool
    }

    private val soundPool = buildSoundPool(context)

    override fun playSound(fileName: String, volume: Float) {
        val soundId = when (fileName) {
            "flip_card_final" -> flipCardEffectId
            "match_effect" -> matchEffectId
            "match_card_init" -> flipInitialCardsEffectId
            else -> throw IllegalArgumentException("Sound file not found: $fileName")
        }
        soundPool.playEffect(soundId, volume)
    }

    override fun release() {
        soundPool.release()
    }

    private fun SoundPool.playEffect(soundId: Int, volume: Float) {
        play(
            soundId,
            volume,
            volume,
            1,
            0,
            1f
        )
    }
}
