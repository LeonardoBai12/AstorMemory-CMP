package io.lb.astormemory.gameover

/**
 * Represents an event that occurs in the game over screen.
 */
sealed interface GameOverEvent {
    /**
     * Represents the game over screen being started, playing the song according to the result.
     */
    data object OnStart : GameOverEvent
}
