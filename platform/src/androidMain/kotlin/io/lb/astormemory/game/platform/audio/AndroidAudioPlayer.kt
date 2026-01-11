package io.lb.astormemory.game.platform.audio

import android.content.Context
import android.media.SoundPool
import io.lb.astormemory.game.platform.R

class AndroidAudioPlayer(context: Context) : AudioPlayer {
    private var flipCardEffectId = 0
    private var matchEffectId = 0
    private var flipInitialCardsEffectId = 0
    private var shuffleId = 0
    private var clickId = 0
    private var optionSelectedId = 0
    val streamIds: MutableMap<Int, Int> = mutableMapOf()

    fun buildSoundPool(context: Context): SoundPool {
        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        flipCardEffectId = soundPool.load(context, R.raw.flip_card_final, 1)
        matchEffectId = soundPool.load(context, R.raw.match_effect, 1)
        flipInitialCardsEffectId = soundPool.load(context, R.raw.match_card_init, 1)
        shuffleId = soundPool.load(context, R.raw.shuffle, 1)
        clickId = soundPool.load(context, R.raw.click, 1)
        optionSelectedId = soundPool.load(context, R.raw.option_selected, 1)
        return soundPool
    }

    private val soundPool = buildSoundPool(context)

    override fun playSound(fileName: String, volume: Float) {
        val soundId = getSoundId(fileName)
        val streamId = soundPool.playEffect(soundId, volume)
        if (fileName == "shuffle") {
            streamIds[soundId] = streamId
        }
    }

    private fun getSoundId(fileName: String): Int = when (fileName) {
        "flip_card_final" -> flipCardEffectId
        "match_effect" -> matchEffectId
        "match_card_init" -> flipInitialCardsEffectId
        "click" -> clickId
        "shuffle" -> shuffleId
        "option_selected" -> optionSelectedId
        else -> throw IllegalArgumentException("Sound file not found: $fileName")
    }

    override fun stopSound(fileName: String) {
        val soundId = getSoundId(fileName)
        if (fileName == "shuffle") {
            val streamId = streamIds[soundId] ?: return
            soundPool.stop(streamId)
            return
        }
        soundPool.stop(soundId)
    }

    override fun release() {
        soundPool.release()
    }

    private fun SoundPool.playEffect(soundId: Int, volume: Float): Int {
        return play(
            soundId,
            volume,
            volume,
            1,
            0,
            1f
        )
    }
}
