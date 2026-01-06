package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.data.datasource.MemoryGameDataSource
import io.lb.astormemory.game.data.resources.AstorResourceProvider
import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.shared.model.AstorCard
import io.lb.astormemory.shared.model.Score
import org.jetbrains.compose.resources.InternalResourceApi

/**
 * Implementation of [MemoryGameRepository] that fetches data from resources and database.
 *
 * @property astorResourceProvider The provider to fetch Astor card resources.
 * @property dataSource The data source to interact with the database.
 */
class MemoryGameRepositoryImpl(
    private val astorResourceProvider: AstorResourceProvider,
    private val dataSource: MemoryGameDataSource
) : MemoryGameRepository {

    @InternalResourceApi
    override suspend fun getAstorPairs(amount: Int): List<AstorCard> {
        return astorResourceProvider.getAstorPairs(amount)
    }

    override suspend fun getScores(): List<Score> {
        return dataSource.getScores()
    }

    override suspend fun getScoresByAmount(amount: Int): List<Score> {
        return dataSource.getScoresByAmount(amount)
    }

    override suspend fun insertScore(score: Int, amount: Int) {
        dataSource.insertScore(score, amount)
    }
}