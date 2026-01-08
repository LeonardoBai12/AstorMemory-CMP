package io.lb.astormemory.navigation

import kotlinx.serialization.Serializable

/**
 * Enum class that represents the different screens of the app.
 *
 * @property Menu The main menu of the app.
 * @property Game The game screen.
 * @property HighScores The high scores screen.
 * @property GameOver The game over screen.
 * @property Settings The settings screen.
 */
sealed interface AstorMemoryRoutes {
    @Serializable
    data object Menu : AstorMemoryRoutes

    @Serializable
    data class Game(val amount: Int) : AstorMemoryRoutes

    @Serializable
    data object HighScores : AstorMemoryRoutes

    @Serializable
    data class GameOver(val score: Int, val amount: Int) : AstorMemoryRoutes

    @Serializable
    data object Settings : AstorMemoryRoutes
}
