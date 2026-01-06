package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.shared.flow.Resource
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to get a list of scores.
 *
 * @property repository The repository to fetch data from.
 */
class GetScoresUseCase(
    private val repository: MemoryGameRepository
) {
    /**
     * Fetches a list of scores.
     *
     * @return A [Flow] of [Resource] of a list of [Score] objects.
     */
    operator fun invoke(): Flow<Resource<List<Score>>> = flow {
        emit(Resource.Loading())
        runCatching {
            val scores = repository.getScores()
            emit(Resource.Success(scores))
        }.getOrElse {
            emit(Resource.Error(it.message.toString()))
        }
    }
}