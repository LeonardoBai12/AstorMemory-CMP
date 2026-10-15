package io.lb.astormemory.game.domain.repository

/**
 * Repository for the Memory Game songs and sound effects.
 *
 * Implementations are responsible for respecting the user's audio settings,
 * so callers don't need to know whether the game is muted or not. The functions that
 * read those settings are suspending.
 */
interface SFXAndMusicRepository {
    /**
     * Play the sound effect of a button being clicked.
     */
    suspend fun playButtonClickedSoundEffect()

    /**
     * Play the sound effect of an option being selected.
     */
    suspend fun playOptionSelectedSoundEffect()

    /**
     * Play the sound effect of a card being flipped.
     */
    suspend fun playCardFlippedSoundEffect()

    /**
     * Play the sound effect of the cards being shuffled.
     */
    suspend fun playShuffleSoundEffect()

    /**
     * Stop the sound effect of the cards being shuffled.
     */
    fun stopShuffleSoundEffect()

    /**
     * Play the sound effect of a pair of cards being matched.
     *
     * @param matches the amount of pairs matched so far.
     * @param totalAmount the total amount of pairs in the game.
     */
    suspend fun playMatchCardSoundEffect(matches: Int, totalAmount: Int)

    /**
     * Play the menu song.
     */
    suspend fun playMenuSong()

    /**
     * Play the high scores song.
     */
    suspend fun playHighScoreSong()

    /**
     * Play the game song according to the amount of cards in the game.
     *
     * @param amount the amount of cards in the game.
     */
    suspend fun playGameSong(amount: Int)

    /**
     * Play the game over song according to the result of the game.
     *
     * @param score the final score of the game.
     * @param amount the amount of cards in the game.
     */
    suspend fun playGameOverSong(score: Int, amount: Int)

    /**
     * Pause any song being played.
     */
    fun pauseSongs()

    /**
     * Resume the last song played.
     */
    suspend fun resumeSong()

    /**
     * Release all the audio resources.
     */
    fun release()
}
