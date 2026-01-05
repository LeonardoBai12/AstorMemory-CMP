package io.lb.astormemory.database.service

import io.lb.astormemory.comon.data.service.DatabaseService
import io.lb.astormemory.database.AstorMemoryDatabase
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class DatabaseServiceImpl(
    database: AstorMemoryDatabase
) : DatabaseService {
    private val queries = database.astorMemoryDatabaseQueries

    override suspend fun insertScore(score: Int, amount: Int) = withContext(Dispatchers.IO) {
        val scores = queries.getScoreEntityByAmount(amount.toLong()).executeAsList()
        
        if (scores.size < MAX_SCORES || score > scores.last().score.toInt()) {
            val id = "${Clock.System.now().toEpochMilliseconds()}-$score"
            queries.insertScoreEntity(
                id = id,
                score = score.toLong(),
                amount = amount.toLong(),
                timeMillis = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun getScores(): List<Score> = withContext(Dispatchers.IO) {
        queries.getAllScoreEntity().executeAsList().map { it.toScore() }
    }

    override suspend fun getScoresByAmount(amount: Int): List<Score> = withContext(Dispatchers.IO) {
        queries.getScoreEntityByAmount(amount.toLong())
            .executeAsList()
            .map { it.toScore() }
            .distinctBy { it.score }
            .take(MAX_SCORES)
    }

    private fun io.lb.astormemory.database.ScoreEntity.toScore() = Score(
        id = id,
        score = score.toInt(),
        amount = amount.toInt(),
        timeMillis = timeMillis
    )

    companion object {
        private const val MAX_SCORES = 10
    }
}
