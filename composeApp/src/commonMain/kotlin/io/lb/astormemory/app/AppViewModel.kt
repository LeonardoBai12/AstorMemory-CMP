package io.lb.astormemory.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the global state of the app.
 *
 * @property settingsRepository the repository of the settings chosen by the user.
 * @property sfxAndMusicRepository the repository to play the songs and sound effects.
 * @property isSystemInDarkMode whether the system theme is dark, used while the user
 * hasn't chosen a theme.
 */
internal class AppViewModel(
    private val settingsRepository: SettingsRepository,
    private val sfxAndMusicRepository: SFXAndMusicRepository,
    private val isSystemInDarkMode: Boolean
) : ViewModel() {

    /**
     * The global state of the app, or null while the settings are still being loaded.
     */
    val state: StateFlow<AppState?> = settingsRepository.settings
        .map { it.toAppState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun onEvent(event: AppEvent) {
        when (event) {
            AppEvent.OnButtonClicked -> viewModelScope.launch {
                sfxAndMusicRepository.playButtonClickedSoundEffect()
            }
            AppEvent.OnOptionSelected -> viewModelScope.launch {
                sfxAndMusicRepository.playOptionSelectedSoundEffect()
            }
        }
    }

    override fun onCleared() {
        sfxAndMusicRepository.release()
        super.onCleared()
    }

    private fun AppSettings.toAppState() = AppState(
        isDarkMode = isDarkMode ?: isSystemInDarkMode,
        isMuted = isMuted,
        isDynamicLayout = isDynamicLayout,
        isSoundEffectsEnabled = isSoundEffectsEnabled,
        cardsPerLine = cardsPerLine,
        cardsPerColumn = cardsPerColumn,
        initialAmount = lastAmount
    )
}
