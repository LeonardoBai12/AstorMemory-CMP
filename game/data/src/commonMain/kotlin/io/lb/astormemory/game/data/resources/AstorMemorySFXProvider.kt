package io.lb.astormemory.game.data.resources

import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.utils.AstorMemoryAudio

/**
 * Provider to play the sound effects of the game, respecting the user's audio settings.
 *
 * @property audioPlayer The player used to play the sound effects.
 * @property settingsDataSource The data source used to read the user's audio settings.
 */
class AstorMemorySFXProvider(
    private val audioPlayer: AudioPlayer,
    private val settingsDataSource: SettingsDataSource,
) {
    suspend fun playButtonClickedSoundEffect() = playIfEnabled { isMuted ->
        AstorMemoryAudio.playClickEffect(isMuted, audioPlayer)
    }

    suspend fun playOptionSelectedSoundEffect() = playIfEnabled { isMuted ->
        AstorMemoryAudio.playOptionSelectEffect(isMuted, audioPlayer)
    }

    suspend fun playCardFlippedSoundEffect() = playIfEnabled { isMuted ->
        AstorMemoryAudio.playFlipEffect(isMuted, audioPlayer)
    }

    suspend fun playShuffleSoundEffect() = playIfEnabled { isMuted ->
        AstorMemoryAudio.playShuffleEffect(isMuted, audioPlayer)
    }

    fun stopShuffleSoundEffect() {
        AstorMemoryAudio.stopShuffleEffect(audioPlayer)
    }

    suspend fun playMatchCardSoundEffect(matches: Int, totalAmount: Int) = playIfEnabled { isMuted ->
        AstorMemoryAudio.playMatchEffect(matches, totalAmount, isMuted, audioPlayer)
    }

    fun release() {
        audioPlayer.release()
    }

    private suspend inline fun playIfEnabled(play: (isMuted: Boolean) -> Unit) {
        val settings = settingsDataSource.getSettings()
        if (settings.isSoundEffectsEnabled) play(settings.isMuted)
    }
}
