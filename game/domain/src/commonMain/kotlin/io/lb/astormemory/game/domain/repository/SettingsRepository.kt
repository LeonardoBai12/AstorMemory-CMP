package io.lb.astormemory.game.domain.repository

import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository for the settings chosen by the user.
 */
interface SettingsRepository {
    /**
     * The current settings, updated whenever one of them changes.
     */
    val settings: StateFlow<AppSettings>

    /**
     * Save whether the dark mode is enabled.
     *
     * @param isDarkMode whether the dark mode is enabled.
     */
    fun setDarkMode(isDarkMode: Boolean)

    /**
     * Save whether the songs are muted.
     *
     * @param isMuted whether the songs are muted.
     */
    fun setMuted(isMuted: Boolean)

    /**
     * Save whether the cards layout adapts to the amount of cards.
     *
     * @param isDynamicLayout whether the cards layout is dynamic.
     */
    fun setDynamicLayout(isDynamicLayout: Boolean)

    /**
     * Save whether the sound effects are enabled.
     *
     * @param isSoundEffectsEnabled whether the sound effects are enabled.
     */
    fun setSoundEffectsEnabled(isSoundEffectsEnabled: Boolean)

    /**
     * Save the amount of cards per line.
     *
     * @param cardsPerLine the amount of cards per line.
     */
    fun setCardsPerLine(cardsPerLine: Int)

    /**
     * Save the amount of cards per column.
     *
     * @param cardsPerColumn the amount of cards per column.
     */
    fun setCardsPerColumn(cardsPerColumn: Int)

    /**
     * Save the last amount of cards chosen to play with.
     *
     * @param amount the amount of cards.
     */
    fun setLastAmount(amount: Int)
}
