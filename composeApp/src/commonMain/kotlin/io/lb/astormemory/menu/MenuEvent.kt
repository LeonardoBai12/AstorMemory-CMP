package io.lb.astormemory.menu

/**
 * Represents an event that occurs in the menu.
 */
sealed interface MenuEvent {
    /**
     * Represents the menu being started, playing the menu song.
     */
    data object OnStart : MenuEvent

    /**
     * Represents a button being clicked.
     */
    data object OnButtonClicked : MenuEvent
}
