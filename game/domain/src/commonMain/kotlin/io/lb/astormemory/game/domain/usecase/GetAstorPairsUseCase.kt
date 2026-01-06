package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.shared.flow.Resource
import io.lb.astormemory.shared.model.AstorCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to get a list of Astor pairs.
 *
 * @property repository The repository to fetch data from.
 */
class GetAstorPairsUseCase(
    private val repository: MemoryGameRepository
) {
    /**
     * Fetches a list of Astor pairs.
     *
     * @param amount The amount of Astor pairs to fetch.
     * @return A [Flow] of [Resource] of a list of [AstorCard] objects.
     */
    operator fun invoke(amount: Int): Flow<Resource<List<AstorCard>>> = flow {
        emit(Resource.Loading())
        runCatching {
            val response = repository.getAstorPairs(amount)
            emit(Resource.Success(response))
        }.getOrElse {
            emit(Resource.Error(it.message.toString()))
        }
    }
}
