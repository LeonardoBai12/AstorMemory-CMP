package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.shared.flow.Resource
import io.lb.astormemory.shared.model.AstorCard
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
class GetAstorPairsUseCaseTest {
    val mocker = Mocker()
    val repository: MemoryGameRepository = mocker.mock()
    lateinit var useCase: GetAstorPairsUseCase

    @BeforeTest
    fun setUp() {
        mocker.reset()
        useCase = GetAstorPairsUseCase(repository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When get astor pairs, expect a list of astor pairs`() = runTest {
        val astorPairs = listOf(
            AstorCard(id = "1", astorId = 1, name = "Astor1", imageUrl = "https://example.com/1.jpg", imageData = byteArrayOf(1, 2, 3)),
            AstorCard(id = "2", astorId = 2, name = "Astor2", imageUrl = "https://example.com/2.jpg", imageData = byteArrayOf(4, 5, 6))
        )
        mocker.everySuspending { repository.getAstorPairs(2) } returns astorPairs

        val states = mutableListOf<Resource<List<AstorCard>>>()
        val result = useCase(2).toCollection(states)

        assertTrue(states.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Success)
        assertEquals(astorPairs, result.last().data)
    }

    @Test
    fun `When get astor pairs, expect an error`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(2) } runs { throw Exception("Error") }

        val states = mutableListOf<Resource<List<AstorCard>>>()
        val result = useCase(2).toCollection(states)

        assertTrue(states.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Error)
        assertEquals("Error", result.last().message)
    }
}
