package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.domain.repository.SettingsRepository
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Implementation of [SettingsRepository] that saves the settings in the app preferences.
 *
 * @property preferences The preferences used to save the settings.
 */
class SettingsRepositoryImpl(
    private val preferences: AppPreferences,
) : SettingsRepository {

    private val _settings = MutableStateFlow(readSettings())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun setDarkMode(isDarkMode: Boolean) {
        preferences.putBoolean(PreferencesKeys.IS_DARK_MODE, isDarkMode)
        _settings.update { it.copy(isDarkMode = isDarkMode) }
    }

    override fun setMuted(isMuted: Boolean) {
        preferences.putBoolean(PreferencesKeys.IS_MUTED, isMuted)
        _settings.update { it.copy(isMuted = isMuted) }
    }

    override fun setDynamicLayout(isDynamicLayout: Boolean) {
        preferences.putBoolean(PreferencesKeys.IS_DYNAMIC_LAYOUT, isDynamicLayout)
        _settings.update { it.copy(isDynamicLayout = isDynamicLayout) }
    }

    override fun setSoundEffectsEnabled(isSoundEffectsEnabled: Boolean) {
        preferences.putBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, isSoundEffectsEnabled)
        _settings.update { it.copy(isSoundEffectsEnabled = isSoundEffectsEnabled) }
    }

    override fun setCardsPerLine(cardsPerLine: Int) {
        preferences.putInt(PreferencesKeys.CARDS_PER_LINE, cardsPerLine)
        _settings.update { it.copy(cardsPerLine = cardsPerLine) }
    }

    override fun setCardsPerColumn(cardsPerColumn: Int) {
        preferences.putInt(PreferencesKeys.CARDS_PER_COLUMN, cardsPerColumn)
        _settings.update { it.copy(cardsPerColumn = cardsPerColumn) }
    }

    override fun setLastAmount(amount: Int) {
        preferences.putInt(PreferencesKeys.LAST_AMOUNT, amount)
        _settings.update { it.copy(lastAmount = amount) }
    }

    private fun readSettings(): AppSettings {
        val defaults = AppSettings()
        return AppSettings(
            isDarkMode = if (preferences.contains(PreferencesKeys.IS_DARK_MODE)) {
                preferences.getBoolean(PreferencesKeys.IS_DARK_MODE, false)
            } else {
                null
            },
            isMuted = preferences.getBoolean(PreferencesKeys.IS_MUTED, defaults.isMuted),
            isDynamicLayout = preferences.getBoolean(
                PreferencesKeys.IS_DYNAMIC_LAYOUT,
                defaults.isDynamicLayout
            ),
            isSoundEffectsEnabled = preferences.getBoolean(
                PreferencesKeys.IS_SOUND_EFFECTS_ENABLED,
                defaults.isSoundEffectsEnabled
            ),
            cardsPerLine = preferences.getInt(PreferencesKeys.CARDS_PER_LINE, defaults.cardsPerLine),
            cardsPerColumn = preferences.getInt(
                PreferencesKeys.CARDS_PER_COLUMN,
                defaults.cardsPerColumn
            ),
            lastAmount = preferences.getInt(PreferencesKeys.LAST_AMOUNT, defaults.lastAmount),
        )
    }
}
