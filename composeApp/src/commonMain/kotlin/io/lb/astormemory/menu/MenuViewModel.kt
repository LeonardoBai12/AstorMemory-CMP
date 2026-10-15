package io.lb.astormemory.menu

import androidx.lifecycle.ViewModel
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository

/**
 * ViewModel for the menu screen.
 *
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 * @property settingsRepository the repository of the settings chosen by the user.
 */
internal class MenuViewModel(
    private val sfxAndMusicRepository: SFXAndMusicRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun onEvent(event: MenuEvent) {
        when (event) {
            MenuEvent.OnStart -> {
                sfxAndMusicRepository.playMenuSong()
            }
            MenuEvent.OnButtonClicked -> {
                sfxAndMusicRepository.playButtonClickedSoundEffect()
            }
            is MenuEvent.OnMutedChanged -> {
                settingsRepository.setMuted(event.isMuted)
            }
            is MenuEvent.OnAmountChanged -> {
                settingsRepository.setLastAmount(event.amount)
            }
        }
    }
}
