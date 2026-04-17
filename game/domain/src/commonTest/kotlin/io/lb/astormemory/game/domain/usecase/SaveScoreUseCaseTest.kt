package io.lb.astormemory.game.domain.usecase

import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(MemoryGameRepository::class)
class SaveScoreUseCaseTest {
    val mocker = Mocker()
    val repository: MemoryGameRepository = mocker.mock()
    lateinit var useCase: SaveScoreUseCase

    @BeforeTest
    fun setUp() {
        mocker.reset()
        useCase = SaveScoreUseCase(repository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When score is greater than zero, expect score to be saved`() = runTest {
        mocker.everySuspending { repository.insertScore(500, 5) } returns Unit

        useCase(500, 5)

        mocker.verifyWithSuspend { repository.insertScore(500, 5) }
    }

    @Test
    fun `When score is zero, expect score not to be saved`() = runTest {
        useCase(0, 5)
        // Calling insertScore on an unstubbed mock throws — test passes only if not called
    }

    @Test
    fun `When score is negative, expect score not to be saved`() = runTest {
        useCase(-100, 5)
    }
}
