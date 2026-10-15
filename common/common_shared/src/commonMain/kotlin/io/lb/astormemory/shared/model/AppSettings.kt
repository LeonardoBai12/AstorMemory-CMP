package io.lb.astormemory.shared.model

/**
 * The settings chosen by the user.
 *
 * @property isDarkMode Whether the dark mode is enabled, or null to follow the system theme.
 * @property isMuted Whether the songs are muted.
 * @property isDynamicLayout Whether the cards layout adapts to the amount of cards.
 * @property isSoundEffectsEnabled Whether the sound effects are enabled.
 * @property cardsPerLine The amount of cards per line when the layout is not dynamic.
 * @property cardsPerColumn The amount of cards per column when the layout is not dynamic.
 * @property lastAmount The last amount of cards chosen to play with.
 */
data class AppSettings(
    val isDarkMode: Boolean? = null,
    val isMuted: Boolean = false,
    val isDynamicLayout: Boolean = true,
    val isSoundEffectsEnabled: Boolean = true,
    val cardsPerLine: Int = 4,
    val cardsPerColumn: Int = 6,
    val lastAmount: Int = 9,
)
