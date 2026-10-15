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

    /**
     * Represents the songs being muted or unmuted.
     *
     * @property isMuted whether the songs are muted.
     */
    data class OnMutedChanged(val isMuted: Boolean) : MenuEvent

    /**
     * Represents the amount of cards to play with being changed.
     *
     * @property amount the amount of cards.
     */
    data class OnAmountChanged(val amount: Int) : MenuEvent
}
