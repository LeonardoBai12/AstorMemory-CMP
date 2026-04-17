package io.lb.astormemory.game

import app.cash.turbine.test
import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.game.domain.usecase.CalculateScoreUseCase
import io.lb.astormemory.game.domain.usecase.GetAstorPairsUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresByAmountUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresUseCase
import io.lb.astormemory.game.domain.usecase.MemoryGameUseCases
import io.lb.astormemory.game.domain.usecase.SaveScoreUseCase
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.shared.model.AstorCard
import io.lb.astormemory.game.GameEvent
import kotlinx.coroutines.Dispatchers
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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@UsesMocks(MemoryGameRepository::class, AudioPlayer::class, AppPreferences::class)
internal class GameViewModelTest {
    val mocker = Mocker()
    val repository: MemoryGameRepository = mocker.mock()
    val audioPlayer: AudioPlayer = mocker.mock()
    val prefs: AppPreferences = mocker.mock()
    lateinit var useCases: MemoryGameUseCases
    lateinit var viewModel: GameViewModel

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
    fun `When the view model is created, expect cards to be fetched`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        assertTrue(viewModel.state.value.isLoading)

        viewModel.state.test {
            val emission = awaitLoaded()
            assertFalse(emission.isLoading)
            assertEquals(10, emission.cards.size)
            assertTrue(emission.cards.none { it.isFlipped || it.isMatched })
            assertNull(emission.message)
        }
    }

    @Test
    fun `When cards fail to load, expect an error message`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } runs { throw Exception("Network error") }
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            val emission = awaitLoaded()
            assertEquals("Network error", emission.message)
            assertTrue(emission.cards.isEmpty())
        }
    }

    @Test
    fun `When a card is flipped, expect the card to be updated`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            awaitLoaded()
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardFlipped(0))
            advanceUntilIdle()

            val emission = awaitItem()
            assertTrue(emission.cards[0].isFlipped)
            assertTrue(emission.cards.drop(1).none { it.isFlipped })
        }
    }

    @Test
    fun `When a card is matched, expect the card to be updated`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            awaitLoaded()
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardMatched(1))
            advanceUntilIdle()

            val emission = awaitItem()
            assertTrue(emission.cards.filter { it.astorCard.astorId == 1 }.all { it.isMatched })
            assertTrue(emission.cards.filter { it.astorCard.astorId != 1 }.none { it.isMatched })
        }
    }

    @Test
    fun `When a card is mismatched, expect the cards to be unflipped`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            awaitLoaded()
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardFlipped(0))
            advanceUntilIdle()

            val emission1 = awaitItem()
            assertTrue(emission1.cards[0].isFlipped)
            assertTrue(emission1.cards.drop(1).none { it.isFlipped })

            viewModel.onEvent(GameEvent.CardFlipped(1))
            advanceUntilIdle()

            val emission2 = awaitItem()
            assertTrue(emission2.cards[0].isFlipped)
            assertTrue(emission2.cards[1].isFlipped)
            assertTrue(emission2.cards.drop(2).none { it.isFlipped })

            viewModel.onEvent(GameEvent.CardMismatched)
            advanceUntilIdle()

            val emission3 = awaitItem()
            assertTrue(emission3.cards.none { it.isFlipped })
        }
    }

    @Test
    fun `When the game is finished, expect the finish event to be emitted`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        mocker.everySuspending { repository.insertScore(isAny(), isAny()) } returns Unit
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.eventFlow.test {
            viewModel.onEvent(GameEvent.GameFinished)
            advanceUntilIdle()

            val event = awaitItem()
            assertTrue(event is GameViewModel.UiEvent.Finish)
            assertEquals(500, (event as GameViewModel.UiEvent.Finish).score)
        }
    }

    @Test
    fun `When the game is restarted, expect cards to be reset`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            awaitLoaded()
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.GameRestarted)
            advanceUntilIdle()

            awaitItem() // intermediate: cards=[], isLoading=true
            val emission = awaitItem() // reloaded state
            assertFalse(emission.isLoading)
            assertEquals(10, emission.cards.size)
            assertEquals(500, emission.score)
            assertTrue(emission.cards.none { it.isFlipped || it.isMatched })
        }
    }

    private fun stubAudio() {
        mocker.every { prefs.getBoolean(isAny(), isAny()) } returns false
        mocker.every { audioPlayer.playSound(isAny(), isAny()) } returns Unit
        mocker.every { audioPlayer.stopSound(isAny()) } returns Unit
    }

    // Drains loading/intermediate states so the caller always starts from the fully loaded state.
    // Real Dispatchers.IO means IO can complete at any point — this makes tests deterministic
    // regardless of whether the first emission is the loading state or the loaded state.
    private suspend fun app.cash.turbine.ReceiveTurbine<GameState>.awaitLoaded(): GameState {
        var state = awaitItem()
        while (state.isLoading || state.cards.isEmpty()) state = awaitItem()
        return state
    }

    private fun astorCards(): List<AstorCard> {
        val singles = listOf(
            AstorCard("1", 1, "https://astorapi.co/api/v2/astor/1", ByteArray(0), "AstorOne"),
            AstorCard("2", 2, "https://astorapi.co/api/v2/astor/2", ByteArray(0), "AstorTwo"),
            AstorCard("3", 3, "https://astorapi.co/api/v2/astor/3", ByteArray(0), "AstorThree"),
            AstorCard("4", 4, "https://astorapi.co/api/v2/astor/4", ByteArray(0), "AstorFour"),
            AstorCard("5", 5, "https://astorapi.co/api/v2/astor/5", ByteArray(0), "AstorFive"),
        )
        return singles + singles
    }
}
