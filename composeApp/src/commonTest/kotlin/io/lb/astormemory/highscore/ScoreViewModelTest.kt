package io.lb.astormemory.highscore

import app.cash.turbine.test
import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.game.domain.usecase.CalculateScoreUseCase
import io.lb.astormemory.game.domain.usecase.GetAstorPairsUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresByAmountUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresUseCase
import io.lb.astormemory.game.domain.usecase.MemoryGameUseCases
import io.lb.astormemory.game.domain.usecase.SaveScoreUseCase
import io.lb.astormemory.shared.model.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@UsesMocks(MemoryGameRepository::class)
internal class ScoreViewModelTest {
    val mocker = Mocker()
    val repository: MemoryGameRepository = mocker.mock()
    lateinit var useCases: MemoryGameUseCases
    lateinit var viewModel: ScoreViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        mocker.reset()
        useCases = MemoryGameUseCases(
            getScoresUseCase = GetScoresUseCase(repository),
            saveScoreUseCase = SaveScoreUseCase(repository),
            getMemoryGameUseCase = GetAstorPairsUseCase(repository),
            calculateScoreUseCase = CalculateScoreUseCase(),
            getScoresByAmountUseCase = GetScoresByAmountUseCase(repository)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When the view model is created, expect scores to be fetched`() = runTest {
        mocker.everySuspending { repository.getScores() } returns scores()

        viewModel = ScoreViewModel(useCases)

        assertTrue(viewModel.state.value.isLoading)
        delay(500)

        viewModel.state.test {
            awaitItem()
            advanceUntilIdle()

            val emission = awaitItem()
            assertEquals(scores(), emission.scores)
            assertNull(emission.message)
        }
    }

    @Test
    fun `When scores fail to load, expect an error message`() = runTest {
        mocker.everySuspending { repository.getScores() } runs { throw Exception("Network error") }

        viewModel = ScoreViewModel(useCases)
        delay(500)

        viewModel.state.test {
            val emission = awaitItem()
            assertTrue(emission.isLoading.not())
            assertEquals("Network error", emission.message)
            assertTrue(emission.scores.isEmpty())
        }
    }

    @Test
    fun `When scores load, expect filters to be populated with distinct amounts plus zero`() = runTest {
        mocker.everySuspending { repository.getScores() } returns scores()

        viewModel = ScoreViewModel(useCases)
        delay(500)

        viewModel.state.test {
            awaitItem()
            advanceUntilIdle()

            val emission = awaitItem()
            assertEquals(listOf(0, 5, 10), emission.filters)
        }
    }

    @Test
    fun `When OnRequestScores with non-zero amount, expect filtered scores`() = runTest {
        val filteredScores = listOf(
            Score(id = "1", score = 1000, amount = 5, timeMillis = 123456789L)
        )
        mocker.everySuspending { repository.getScores() } returns scores()
        mocker.everySuspending { repository.getScoresByAmount(5) } returns filteredScores

        viewModel = ScoreViewModel(useCases)
        delay(500)

        viewModel.state.test {
            awaitItem() // initial loaded state
            viewModel.onEvent(ScoresEvent.OnRequestScores(amount = 5))
            delay(500)

            val filtered = awaitItem()
            assertTrue(filtered.isLoading.not())
            assertEquals(filteredScores, filtered.scores)
            assertNull(filtered.message)
        }
    }

    @Test
    fun `When OnRequestScores with zero amount, expect all scores`() = runTest {
        mocker.everySuspending { repository.getScores() } returns scores()

        viewModel = ScoreViewModel(useCases)
        delay(500)

        viewModel.state.test {
            awaitItem() // initial loaded state
            viewModel.onEvent(ScoresEvent.OnRequestScores(amount = 0))
            delay(500)

            val emission = awaitItem()
            assertTrue(emission.isLoading.not())
            assertEquals(scores(), emission.scores)
            assertNull(emission.message)
        }
    }

    @Test
    fun `When scores by amount fail, expect an error message`() = runTest {
        mocker.everySuspending { repository.getScores() } returns scores()
        mocker.everySuspending { repository.getScoresByAmount(5) } runs { throw Exception("DB error") }

        viewModel = ScoreViewModel(useCases)
        delay(500)

        viewModel.state.test {
            awaitItem() // initial loaded state
            viewModel.onEvent(ScoresEvent.OnRequestScores(amount = 5))
            delay(500)

            val emission = awaitItem()
            assertTrue(emission.isLoading.not())
            assertEquals("DB error", emission.message)
            assertTrue(emission.scores.isEmpty())
        }
    }

    private fun scores() = listOf(
        Score(id = "1", score = 1000, amount = 5, timeMillis = 123456789L),
        Score(id = "2", score = 2000, amount = 10, timeMillis = 987654321L)
    )
}
