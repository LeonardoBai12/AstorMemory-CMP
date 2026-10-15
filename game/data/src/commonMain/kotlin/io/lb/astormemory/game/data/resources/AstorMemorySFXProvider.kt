package io.lb.astormemory.game.data.resources

import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.AstorMemoryAudio
import io.lb.astormemory.game.platform.utils.PreferencesKeys

/**
 * Provider to play the sound effects of the game, respecting the user's audio settings.
 *
 * @property audioPlayer The player used to play the sound effects.
 * @property preferences The preferences used to read the user's audio settings.
 */
class AstorMemorySFXProvider(
    private val audioPlayer: AudioPlayer,
    private val preferences: AppPreferences,
) {
    private val isMuted: Boolean
        get() = preferences.getBoolean(PreferencesKeys.IS_MUTED, false)

    private val isSoundEffectsEnabled: Boolean
        get() = preferences.getBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, true)

    fun playButtonClickedSoundEffect() = playIfEnabled {
        AstorMemoryAudio.playClickEffect(isMuted, audioPlayer)
    }

    fun playOptionSelectedSoundEffect() = playIfEnabled {
        AstorMemoryAudio.playOptionSelectEffect(isMuted, audioPlayer)
    }

    fun playCardFlippedSoundEffect() = playIfEnabled {
        AstorMemoryAudio.playFlipEffect(isMuted, audioPlayer)
    }

    fun playShuffleSoundEffect() = playIfEnabled {
        AstorMemoryAudio.playShuffleEffect(isMuted, audioPlayer)
    }

    fun stopShuffleSoundEffect() {
        AstorMemoryAudio.stopShuffleEffect(audioPlayer)
    }

    fun playMatchCardSoundEffect(matches: Int, totalAmount: Int) = playIfEnabled {
        AstorMemoryAudio.playMatchEffect(matches, totalAmount, isMuted, audioPlayer)
    }

    fun release() {
        audioPlayer.release()
    }

    private inline fun playIfEnabled(play: () -> Unit) {
        if (isSoundEffectsEnabled) play()
    }
}
