package io.lb.astormemory.settings

/**
 * Represents an event that occurs in the settings screen.
 */
sealed interface SettingsEvent {
    /**
     * Represents an option being changed.
     */
    data object OnOptionChanged : SettingsEvent
}
