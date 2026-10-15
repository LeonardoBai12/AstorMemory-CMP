package io.lb.astormemory.menu

import androidx.lifecycle.ViewModel
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository

/**
 * ViewModel for the menu screen.
 *
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 */
internal class MenuViewModel(
    private val sfxAndMusicRepository: SFXAndMusicRepository
) : ViewModel() {

    fun onEvent(event: MenuEvent) {
        when (event) {
            MenuEvent.OnStart -> {
                sfxAndMusicRepository.playMenuSong()
            }
            MenuEvent.OnButtonClicked -> {
                sfxAndMusicRepository.playButtonClickedSoundEffect()
            }
        }
    }
}
