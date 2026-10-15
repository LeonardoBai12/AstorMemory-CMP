package io.lb.astormemory.game.domain.repository

import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository for the settings chosen by the user.
 */
interface SettingsRepository {
    /**
     * The current settings, updated whenever one of them changes.
     */
    val settings: Flow<AppSettings>

    /**
     * Save whether the dark mode is enabled.
     *
     * @param isDarkMode whether the dark mode is enabled.
     */
    suspend fun setDarkMode(isDarkMode: Boolean)

    /**
     * Save whether the songs are muted.
     *
     * @param isMuted whether the songs are muted.
     */
    suspend fun setMuted(isMuted: Boolean)

    /**
     * Save whether the cards layout adapts to the amount of cards.
     *
     * @param isDynamicLayout whether the cards layout is dynamic.
     */
    suspend fun setDynamicLayout(isDynamicLayout: Boolean)

    /**
     * Save whether the sound effects are enabled.
     *
     * @param isSoundEffectsEnabled whether the sound effects are enabled.
     */
    suspend fun setSoundEffectsEnabled(isSoundEffectsEnabled: Boolean)

    /**
     * Save the amount of cards per line.
     *
     * @param cardsPerLine the amount of cards per line.
     */
    suspend fun setCardsPerLine(cardsPerLine: Int)

    /**
     * Save the amount of cards per column.
     *
     * @param cardsPerColumn the amount of cards per column.
     */
    suspend fun setCardsPerColumn(cardsPerColumn: Int)

    /**
     * Save the last amount of cards chosen to play with.
     *
     * @param amount the amount of cards.
     */
    suspend fun setLastAmount(amount: Int)
}
