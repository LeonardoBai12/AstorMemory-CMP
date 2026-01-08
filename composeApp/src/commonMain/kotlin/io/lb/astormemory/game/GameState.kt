package io.lb.presentation.game

import io.lb.astormemory.game.ds.model.GameCard

/**
 * Represents the state of the game.
 */
internal data class GameState(
    val cards: List<GameCard> = emptyList(),
    val currentCombo: Int = 0,
    val message: String? = null,
    val isLoading: Boolean = true,
    val score: Int = 0,
    val amount: Int = 0,
)
