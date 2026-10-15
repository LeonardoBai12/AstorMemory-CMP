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

    override fun playButtonClickedSoundEffect() {
        sfxProvider.playButtonClickedSoundEffect()
    }

    override fun playOptionSelectedSoundEffect() {
        sfxProvider.playOptionSelectedSoundEffect()
    }

    override fun playCardFlippedSoundEffect() {
        sfxProvider.playCardFlippedSoundEffect()
    }

    override fun playShuffleSoundEffect() {
        sfxProvider.playShuffleSoundEffect()
    }

    override fun stopShuffleSoundEffect() {
        sfxProvider.stopShuffleSoundEffect()
    }

    override fun playMatchCardSoundEffect(matches: Int, totalAmount: Int) {
        sfxProvider.playMatchCardSoundEffect(matches, totalAmount)
    }

    override fun playMenuSong() {
        songsProvider.playMenuSong()
    }

    override fun playHighScoreSong() {
        songsProvider.playHighScoreSong()
    }

    override fun playGameSong(amount: Int) {
        songsProvider.playGameSong(amount)
    }

    override fun playGameOverSong(score: Int, amount: Int) {
        songsProvider.playGameOverSong(score, amount)
    }

    override fun pauseSongs() {
        songsProvider.pauseSongs()
    }

    override fun resumeSong() {
        songsProvider.resumeSong()
    }

    override fun release() {
        sfxProvider.release()
        songsProvider.release()
    }
}
