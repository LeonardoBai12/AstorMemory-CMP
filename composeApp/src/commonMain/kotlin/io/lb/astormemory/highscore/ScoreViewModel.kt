package io.lb.astormemory.highscore

import androidx.lifecycle.ViewModel
import io.lb.astormemory.game.domain.usecase.MemoryGameUseCases
import io.lb.astormemory.shared.flow.Resource
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

private const val DEBOUNCE_DELAY = 300L
private const val AMOUNT_TAKEN = 10

/**
 * ViewModel for the scores screen.
 *
 * @property useCases the use cases for the memory game.
 */
internal class ScoreViewModel(
    private val useCases: MemoryGameUseCases
) : ViewModel() {
    
    private val _state: MutableStateFlow<ScoreState> = MutableStateFlow(ScoreState())
    val state: StateFlow<ScoreState> = _state

    private var getScoresJob: Job? = null
    private val scores = mutableListOf<Score>()

    init {
        getScores()
    }

    fun onEvent(event: ScoresEvent) {
        when (event) {
            is ScoresEvent.OnRequestScores -> {
                if (event.amount != 0) {
                    getScoresByAmount(event.amount)
                } else {
                    getScores()
                }
            }
        }
    }

    private fun getScoresByAmount(amount: Int) {
        getScoresJob?.cancel()
        getScoresJob = useCases.getScoresByAmountUseCase(amount).onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = resource.message,
                            scores = emptyList()
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            message = null,
                            scores = emptyList()
                        )
                    }
                }

                is Resource.Success -> {
                    scores.clear()
                    scores.addAll(resource.data ?: emptyList())
                    delay(DEBOUNCE_DELAY)
                    _state.update {
                        it.copy(
                            scores = scores,
                            isLoading = false,
                            message = null
                        )
                    }
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    private fun getScores() {
        getScoresJob?.cancel()
        getScoresJob = useCases.getScoresUseCase().onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = resource.message,
                            scores = emptyList()
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            message = null,
                            scores = emptyList()
                        )
                    }
                }

                is Resource.Success -> {
                    scores.clear()
                    scores.addAll(resource.data ?: emptyList())
                    delay(DEBOUNCE_DELAY)

                    if (state.value.filters.isNotEmpty()) {
                        _state.update {
                            it.copy(
                                scores = scores.take(AMOUNT_TAKEN),
                                isLoading = false,
                                message = null
                            )
                        }
                        return@onEach
                    }
                    val availableAmounts = scores.map { it.amount }.distinct().toMutableList()
                    availableAmounts.add(0)

                    _state.update {
                        it.copy(
                            scores = scores.take(AMOUNT_TAKEN),
                            isLoading = false,
                            filters = availableAmounts.sorted(),
                            message = null
                        )
                    }
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }
}
