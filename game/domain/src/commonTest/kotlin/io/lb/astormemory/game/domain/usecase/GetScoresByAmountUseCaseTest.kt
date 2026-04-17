package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.shared.flow.Resource
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@UsesMocks(MemoryGameRepository::class)
class GetScoresByAmountUseCaseTest {
    val mocker = Mocker()
    val repository: MemoryGameRepository = mocker.mock()
    lateinit var useCase: GetScoresByAmountUseCase

    @BeforeTest
    fun setUp() {
        mocker.reset()
        useCase = GetScoresByAmountUseCase(repository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When get scores by amount, expect a score list for that amount`() = runTest {
        val scores = listOf(
            Score(id = "1", score = 1000, amount = 5, timeMillis = 123456789L),
            Score(id = "2", score = 1500, amount = 5, timeMillis = 987654321L)
        )
        mocker.everySuspending { repository.getScoresByAmount(5) } returns scores

        val states = mutableListOf<Resource<List<Score>>>()
        val result = useCase(5).toCollection(states)

        assertTrue(states.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Success)
        assertEquals(scores, result.last().data)
    }

    @Test
    fun `When get scores by amount, expect an error`() = runTest {
        mocker.everySuspending { repository.getScoresByAmount(5) } runs { throw Exception("Error") }

        val states = mutableListOf<Resource<List<Score>>>()
        val result = useCase(5).toCollection(states)

        assertTrue(states.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Error)
        assertEquals("Error", result.last().message)
    }
}
