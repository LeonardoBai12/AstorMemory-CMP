package io.lb.astormemory.dynamic

fun calculateCardsPerLine(amount: Int): Int {
    return when {
        amount <= 9 -> 3
        amount <= 14 -> 4
        amount <= 17 -> 5
        else -> 6
    }
}

fun calculateCardsPerColumn(amount: Int): Int {
    return when {
        amount <= 6 -> 5
        amount <= 7 -> 6
        amount <= 9 -> 7
        amount <= 10 -> 6
        amount <= 12 -> 7
        amount <= 14 -> 8
        amount <= 15 -> 7
        amount <= 16 -> 8
        amount <= 17 -> 8
        amount <= 18 -> 7
        amount <= 20 -> 8
        amount <= 21 -> 8
        else -> 9
    }
}
