package io.lb.astormemory.settings

import androidx.lifecycle.ViewModel
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository

/**
 * ViewModel for the settings screen.
 *
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 * @property settingsRepository the repository of the settings chosen by the user.
 */
internal class SettingsViewModel(
    private val sfxAndMusicRepository: SFXAndMusicRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnDarkModeChanged -> {
                sfxAndMusicRepository.playCardFlippedSoundEffect()
                settingsRepository.setDarkMode(event.isDarkMode)
            }
            is SettingsEvent.OnDynamicLayoutChanged -> {
                sfxAndMusicRepository.playCardFlippedSoundEffect()
                settingsRepository.setDynamicLayout(event.isDynamicLayout)
            }
            is SettingsEvent.OnSoundEffectsEnabledChanged -> {
                settingsRepository.setSoundEffectsEnabled(event.isSoundEffectsEnabled)
                sfxAndMusicRepository.playCardFlippedSoundEffect()
            }
            is SettingsEvent.OnCardsPerLineChanged -> {
                settingsRepository.setCardsPerLine(event.cardsPerLine)
            }
            is SettingsEvent.OnCardsPerColumnChanged -> {
                settingsRepository.setCardsPerColumn(event.cardsPerColumn)
            }
        }
    }
}
