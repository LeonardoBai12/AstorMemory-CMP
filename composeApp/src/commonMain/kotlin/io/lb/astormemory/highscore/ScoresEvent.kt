package io.lb.astormemory.highscore

sealed class ScoresEvent {
    data class OnRequestScores(val amount: Int) : ScoresEvent()
}
