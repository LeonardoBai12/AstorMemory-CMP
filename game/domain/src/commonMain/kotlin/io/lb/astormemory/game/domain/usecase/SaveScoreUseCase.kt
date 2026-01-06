package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository

/**
 * Use case to save a score.
 *
 * @property repository The repository to save the score to.
 */
class SaveScoreUseCase(
    private val repository: MemoryGameRepository
) {
    /**
     * Saves a score.
     *
     * @param score The score to save.
     * @param amount The amount of cards in the game.
     *
     * @return A [Flow] of [Resource] of a [Unit] object.
     */
    suspend operator fun invoke(score: Int, amount: Int) {
        if (score > 0) {
            repository.insertScore(score, amount)
        }
    }
}