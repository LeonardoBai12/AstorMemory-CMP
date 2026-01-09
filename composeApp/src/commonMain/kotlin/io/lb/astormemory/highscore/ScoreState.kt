package io.lb.astormemory.highscore

import io.lb.astormemory.shared.model.Score

internal data class ScoreState(
    val scores: List<Score> = emptyList(),
    val filters: List<Int> = emptyList(),
    val message: String? = null,
    val isLoading: Boolean = true,
)
