package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.domain.repository.SettingsRepository
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [SettingsRepository] that saves the settings in a DataStore.
 *
 * @property dataSource The data source of the saved settings.
 */
class SettingsRepositoryImpl(
    private val dataSource: SettingsDataSource,
) : SettingsRepository {

    override val settings: Flow<AppSettings> = dataSource.settings

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        dataSource.setDarkMode(isDarkMode)
    }

    override suspend fun setMuted(isMuted: Boolean) {
        dataSource.setMuted(isMuted)
    }

    override suspend fun setDynamicLayout(isDynamicLayout: Boolean) {
        dataSource.setDynamicLayout(isDynamicLayout)
    }

    override suspend fun setSoundEffectsEnabled(isSoundEffectsEnabled: Boolean) {
        dataSource.setSoundEffectsEnabled(isSoundEffectsEnabled)
    }

    override suspend fun setCardsPerLine(cardsPerLine: Int) {
        dataSource.setCardsPerLine(cardsPerLine)
    }

    override suspend fun setCardsPerColumn(cardsPerColumn: Int) {
        dataSource.setCardsPerColumn(cardsPerColumn)
    }

    override suspend fun setLastAmount(amount: Int) {
        dataSource.setLastAmount(amount)
    }
}
