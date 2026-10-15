package io.lb.astormemory.game.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Data source for the settings chosen by the user.
 *
 * @property dataStore The DataStore where the settings are saved.
 */
class SettingsDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    /**
     * The saved settings, emitted again whenever one of them changes.
     */
    val settings: Flow<AppSettings> = dataStore.data.map { it.toAppSettings() }

    /**
     * Get the saved settings, including any change already saved.
     *
     * @return The saved settings.
     */
    suspend fun getSettings(): AppSettings = settings.first()

    suspend fun setDarkMode(isDarkMode: Boolean) = save(PreferencesKeys.IS_DARK_MODE, isDarkMode)

    suspend fun setMuted(isMuted: Boolean) = save(PreferencesKeys.IS_MUTED, isMuted)

    suspend fun setDynamicLayout(isDynamicLayout: Boolean) =
        save(PreferencesKeys.IS_DYNAMIC_LAYOUT, isDynamicLayout)

    suspend fun setSoundEffectsEnabled(isSoundEffectsEnabled: Boolean) =
        save(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, isSoundEffectsEnabled)

    suspend fun setCardsPerLine(cardsPerLine: Int) = save(PreferencesKeys.CARDS_PER_LINE, cardsPerLine)

    suspend fun setCardsPerColumn(cardsPerColumn: Int) =
        save(PreferencesKeys.CARDS_PER_COLUMN, cardsPerColumn)

    suspend fun setLastAmount(amount: Int) = save(PreferencesKeys.LAST_AMOUNT, amount)

    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    private fun Preferences.toAppSettings(): AppSettings {
        val defaults = AppSettings()
        return AppSettings(
            isDarkMode = this[PreferencesKeys.IS_DARK_MODE],
            isMuted = this[PreferencesKeys.IS_MUTED] ?: defaults.isMuted,
            isDynamicLayout = this[PreferencesKeys.IS_DYNAMIC_LAYOUT] ?: defaults.isDynamicLayout,
            isSoundEffectsEnabled = this[PreferencesKeys.IS_SOUND_EFFECTS_ENABLED]
                ?: defaults.isSoundEffectsEnabled,
            cardsPerLine = this[PreferencesKeys.CARDS_PER_LINE] ?: defaults.cardsPerLine,
            cardsPerColumn = this[PreferencesKeys.CARDS_PER_COLUMN] ?: defaults.cardsPerColumn,
            lastAmount = this[PreferencesKeys.LAST_AMOUNT] ?: defaults.lastAmount,
        )
    }
}
