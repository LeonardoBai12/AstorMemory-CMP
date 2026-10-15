package io.lb.astormemory.settings

import androidx.lifecycle.ViewModel
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository

/**
 * ViewModel for the settings screen.
 *
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 */
internal class SettingsViewModel(
    private val sfxAndMusicRepository: SFXAndMusicRepository
) : ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnOptionChanged -> {
                sfxAndMusicRepository.playCardFlippedSoundEffect()
            }
        }
    }
}
