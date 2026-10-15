package io.lb.astormemory.game

/**
 * Represents an event that occurs in the game.
 */
sealed interface GameEvent {
    /**
     * Represents the request for games.
     */
    data object OnRequestGames : GameEvent

    /**
     * Represents a card being flipped.
     *
     * @property index The index of the card that was flipped.
     */
    data class CardFlipped(val index: Int) : GameEvent

    /**
     * Represents a card being matched.
     *
     * @property id The id of the card that was matched.
     */
    data class CardMatched(val id: Int) : GameEvent

    /**
     * Represents a card being mismatched.
     */
    data object CardMismatched : GameEvent

    /**
     * Represents the game being finished.
     */
    data object GameFinished : GameEvent

    /**
     * Represents the game being restarted.
     */
    data object GameRestarted : GameEvent

    /**
     * Represents the game being started, playing the song according to the amount of cards.
     */
    data object GameStarted : GameEvent

    /**
     * Represents the songs being paused.
     */
    data object SongsPaused : GameEvent

    /**
     * Represents the last song being resumed.
     */
    data object SongResumed : GameEvent
}
