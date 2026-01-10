package io.lb.astormemory.dynamic

actual fun calculateCardsPerLine(amount: Int): Int {
    return when {
        amount <= 9 -> 3
        amount <= 16 -> 4
        amount <= 20 -> 5
        else -> 6
    }
}

actual fun calculateCardsPerColumn(amount: Int): Int {
    return when {
        amount <= 6 -> 5
        amount <= 9 -> 6
        amount <= 12 -> 6
        amount <= 14 -> 7
        amount <= 16 -> 8
        amount <= 17 -> 7
        amount <= 20 -> 8
        amount <= 24 -> 8
        else -> 9
    }
}