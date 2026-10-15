package io.lb.astormemory.game.domain.repository

/**
 * Repository for the Memory Game songs and sound effects.
 *
 * Implementations are responsible for respecting the user's audio settings,
 * so callers don't need to know whether the game is muted or not.
 */
interface SFXAndMusicRepository {
    /**
     * Play the sound effect of a button being clicked.
     */
    fun playButtonClickedSoundEffect()

    /**
     * Play the sound effect of an option being selected.
     */
    fun playOptionSelectedSoundEffect()

    /**
     * Play the sound effect of a card being flipped.
     */
    fun playCardFlippedSoundEffect()

    /**
     * Play the sound effect of the cards being shuffled.
     */
    fun playShuffleSoundEffect()

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
    fun playMatchCardSoundEffect(matches: Int, totalAmount: Int)

    /**
     * Play the menu song.
     */
    fun playMenuSong()

    /**
     * Play the high scores song.
     */
    fun playHighScoreSong()

    /**
     * Play the game song according to the amount of cards in the game.
     *
     * @param amount the amount of cards in the game.
     */
    fun playGameSong(amount: Int)

    /**
     * Play the game over song according to the result of the game.
     *
     * @param score the final score of the game.
     * @param amount the amount of cards in the game.
     */
    fun playGameOverSong(score: Int, amount: Int)

    /**
     * Pause any song being played.
     */
    fun pauseSongs()

    /**
     * Resume the last song played.
     */
    fun resumeSong()

    /**
     * Release all the audio resources.
     */
    fun release()
}
