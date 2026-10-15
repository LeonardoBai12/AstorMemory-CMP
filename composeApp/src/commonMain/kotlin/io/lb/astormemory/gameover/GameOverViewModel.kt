package io.lb.astormemory.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the game over screen.
 *
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 * @property score the final score of the game.
 * @property amount the amount of cards in the game.
 */
internal class GameOverViewModel(
    private val sfxAndMusicRepository: SFXAndMusicRepository,
    private val score: Int,
    private val amount: Int
) : ViewModel() {

    fun onEvent(event: GameOverEvent) {
        when (event) {
            GameOverEvent.OnStart -> viewModelScope.launch {
                sfxAndMusicRepository.playGameOverSong(score, amount)
            }
        }
    }
}
