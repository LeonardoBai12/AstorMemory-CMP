package io.lb.astormemory.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
import kotlinx.coroutines.launch

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
            MenuEvent.OnStart -> viewModelScope.launch {
                sfxAndMusicRepository.playMenuSong()
            }
            MenuEvent.OnButtonClicked -> viewModelScope.launch {
                sfxAndMusicRepository.playButtonClickedSoundEffect()
            }
            is MenuEvent.OnMutedChanged -> viewModelScope.launch {
                settingsRepository.setMuted(event.isMuted)
                sfxAndMusicRepository.playButtonClickedSoundEffect()
            }
            is MenuEvent.OnAmountChanged -> viewModelScope.launch {
                settingsRepository.setLastAmount(event.amount)
            }
        }
    }
}
