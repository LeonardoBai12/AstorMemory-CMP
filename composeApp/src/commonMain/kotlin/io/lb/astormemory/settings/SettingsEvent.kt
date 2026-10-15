package io.lb.astormemory.settings

/**
 * Represents an event that occurs in the settings screen.
 */
sealed interface SettingsEvent {
    /**
     * Represents the dark mode being enabled or disabled.
     *
     * @property isDarkMode whether the dark mode is enabled.
     */
    data class OnDarkModeChanged(val isDarkMode: Boolean) : SettingsEvent

    /**
     * Represents the dynamic layout being enabled or disabled.
     *
     * @property isDynamicLayout whether the cards layout is dynamic.
     */
    data class OnDynamicLayoutChanged(val isDynamicLayout: Boolean) : SettingsEvent

    /**
     * Represents the sound effects being enabled or disabled.
     *
     * @property isSoundEffectsEnabled whether the sound effects are enabled.
     */
    data class OnSoundEffectsEnabledChanged(val isSoundEffectsEnabled: Boolean) : SettingsEvent

    /**
     * Represents the amount of cards per line being changed.
     *
     * @property cardsPerLine the amount of cards per line.
     */
    data class OnCardsPerLineChanged(val cardsPerLine: Int) : SettingsEvent

    /**
     * Represents the amount of cards per column being changed.
     *
     * @property cardsPerColumn the amount of cards per column.
     */
    data class OnCardsPerColumnChanged(val cardsPerColumn: Int) : SettingsEvent
}
