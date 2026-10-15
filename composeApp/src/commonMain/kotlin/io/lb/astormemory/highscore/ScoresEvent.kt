package io.lb.astormemory.highscore

sealed class ScoresEvent {
    data class OnRequestScores(val amount: Int) : ScoresEvent()
    data object OnPlaySong : ScoresEvent()
    data object OnPauseSongs : ScoresEvent()
    data object OnResumeSong : ScoresEvent()
}
