package io.lb.astormemory.app

import io.lb.astormemory.dynamic.calculateCardsPerColumn
import io.lb.astormemory.dynamic.calculateCardsPerLine

/**
 * Global state of the app, shared between the screens.
 */
data class AppState(
    val isDarkMode: Boolean,
    val isMuted: Boolean,
    val isDynamicLayout: Boolean,
    val isSoundEffectsEnabled: Boolean,
    val cardsPerLine: Int,
    val cardsPerColumn: Int,
    val initialAmount: Int
)

fun getEffectiveCardsPerLine(appState: AppState, amount: Int): Int {
    return if (appState.isDynamicLayout) calculateCardsPerLine(amount) else appState.cardsPerLine
}

fun getEffectiveCardsPerColumn(appState: AppState, amount: Int): Int {
    return if (appState.isDynamicLayout) calculateCardsPerColumn(amount) else appState.cardsPerColumn
}
