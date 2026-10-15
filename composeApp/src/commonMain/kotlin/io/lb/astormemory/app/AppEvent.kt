package io.lb.astormemory.app

/**
 * Represents an event that occurs anywhere in the app.
 */
sealed interface AppEvent {
    /**
     * Represents a design system button being clicked.
     */
    data object OnButtonClicked : AppEvent

    /**
     * Represents a design system option being selected.
     */
    data object OnOptionSelected : AppEvent
}
