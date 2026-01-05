package io.lb.astormemory.comon.data.service

import io.lb.astormemory.shared.model.Score

interface DatabaseService {
    suspend fun insertScore(score: Int, amount: Int)
    suspend fun getScores(): List<Score>
    suspend fun getScoresByAmount(amount: Int): List<Score>
}
