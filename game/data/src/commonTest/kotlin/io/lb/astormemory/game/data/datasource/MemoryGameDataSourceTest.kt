package io.lb.astormemory.game.data.datasource

import io.lb.astormemory.comon.data.service.DatabaseService
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@UsesMocks(DatabaseService::class)
class MemoryGameDataSourceTest {
    val mocker = Mocker()
    val databaseService: DatabaseService = mocker.mock()
    lateinit var dataSource: MemoryGameDataSource

    @BeforeTest
    fun setUp() {
        mocker.reset()
        dataSource = MemoryGameDataSource(databaseService)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When get scores, expect a score list`() = runTest {
        val scores = listOf(
            Score(id = "1", score = 1000, amount = 5, timeMillis = 123456789L),
            Score(id = "2", score = 2000, amount = 10, timeMillis = 987654321L)
        )
        mocker.everySuspending { databaseService.getScores() } returns scores

        val result = dataSource.getScores()

        assertEquals(scores, result)
    }

    @Test
    fun `When get scores by amount, expect a score list for that amount`() = runTest {
        val scores = listOf(
            Score(id = "1", score = 1000, amount = 5, timeMillis = 123456789L),
            Score(id = "2", score = 1500, amount = 5, timeMillis = 987654321L)
        )
        mocker.everySuspending { databaseService.getScoresByAmount(5) } returns scores

        val result = dataSource.getScoresByAmount(5)

        assertEquals(scores, result)
    }

    @Test
    fun `When insert score, expect score to be inserted`() = runTest {
        mocker.everySuspending { databaseService.insertScore(1000, 5) } returns Unit

        dataSource.insertScore(1000, 5)

        mocker.verifyWithSuspend { databaseService.insertScore(1000, 5) }
    }
}
