package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.data.resources.AstorMemorySFXProvider
import io.lb.astormemory.game.data.resources.AstorMemorySongsProvider
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository

/**
 * Implementation of [SFXAndMusicRepository] that plays songs and sound effects from resources.
 *
 * @property sfxProvider The provider to play the sound effects.
 * @property songsProvider The provider to play the songs.
 */
class SFXAndMusicRepositoryImpl(
    private val sfxProvider: AstorMemorySFXProvider,
    private val songsProvider: AstorMemorySongsProvider,
) : SFXAndMusicRepository {

    override suspend fun playButtonClickedSoundEffect() {
        sfxProvider.playButtonClickedSoundEffect()
    }

    override suspend fun playOptionSelectedSoundEffect() {
        sfxProvider.playOptionSelectedSoundEffect()
    }

    override suspend fun playCardFlippedSoundEffect() {
        sfxProvider.playCardFlippedSoundEffect()
    }

    override suspend fun playShuffleSoundEffect() {
        sfxProvider.playShuffleSoundEffect()
    }

    override fun stopShuffleSoundEffect() {
        sfxProvider.stopShuffleSoundEffect()
    }

    override suspend fun playMatchCardSoundEffect(matches: Int, totalAmount: Int) {
        sfxProvider.playMatchCardSoundEffect(matches, totalAmount)
    }

    override suspend fun playMenuSong() {
        songsProvider.playMenuSong()
    }

    override suspend fun playHighScoreSong() {
        songsProvider.playHighScoreSong()
    }

    override suspend fun playGameSong(amount: Int) {
        songsProvider.playGameSong(amount)
    }

    override suspend fun playGameOverSong(score: Int, amount: Int) {
        songsProvider.playGameOverSong(score, amount)
    }

    override fun pauseSongs() {
        songsProvider.pauseSongs()
    }

    override suspend fun resumeSong() {
        songsProvider.resumeSong()
    }

    override fun release() {
        sfxProvider.release()
        songsProvider.release()
    }
}
