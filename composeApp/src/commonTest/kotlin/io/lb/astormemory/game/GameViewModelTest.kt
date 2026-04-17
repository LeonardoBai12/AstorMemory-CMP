package io.lb.astormemory.game

import app.cash.turbine.test
import io.lb.astormemory.game.domain.repository.MemoryGameRepository
import io.lb.astormemory.game.domain.usecase.CalculateScoreUseCase
import io.lb.astormemory.game.domain.usecase.GetAstorPairsUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresByAmountUseCase
import io.lb.astormemory.game.domain.usecase.GetScoresUseCase
import io.lb.astormemory.game.domain.usecase.MemoryGameUseCases
import io.lb.astormemory.game.domain.usecase.SaveScoreUseCase
import io.lb.astormemory.game.ds.model.GameCard
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
import kotlin.test.assertNotEquals
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
            assertTrue(emission.isLoading.not())
            assertTrue(emission.cards.equalsCards(gameCards()))
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
            val emission = awaitLoaded()
            assertTrue(emission.cards.equalsCards(gameCards()))
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardFlipped(0))
            advanceUntilIdle()

            val emission2 = awaitItem()
            assertTrue(
                emission2.cards.equalsCards(
                    gameCards().mapIndexed { index, card ->
                        if (index == 0) card.copy(isFlipped = true) else card
                    }
                )
            )
        }
    }

    @Test
    fun `When a card is matched, expect the card to be updated`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            val emission = awaitLoaded()
            assertTrue(emission.cards.equalsCards(gameCards()))
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardMatched(1))
            advanceUntilIdle()

            val emission2 = awaitItem()
            assertTrue(
                emission2.cards.equalsCards(
                    gameCards().map { card ->
                        if (card.astorCard.astorId == 1) card.copy(isMatched = true) else card
                    }
                )
            )
        }
    }

    @Test
    fun `When a card is mismatched, expect the cards to be unflipped`() = runTest {
        mocker.everySuspending { repository.getAstorPairs(5) } returns astorCards()
        stubAudio()

        viewModel = GameViewModel(useCases, audioPlayer, prefs, 5)

        viewModel.state.test {
            val emission = awaitLoaded()
            assertTrue(emission.cards.equalsCards(gameCards()))
            advanceUntilIdle()

            viewModel.onEvent(GameEvent.CardFlipped(0))
            advanceUntilIdle()

            val emission2 = awaitItem()
            assertTrue(
                emission2.cards.equalsCards(
                    gameCards().mapIndexed { index, card ->
                        if (index == 0) card.copy(isFlipped = true) else card
                    }
                )
            )

            viewModel.onEvent(GameEvent.CardFlipped(1))
            advanceUntilIdle()

            val emission3 = awaitItem()
            assertTrue(
                emission3.cards.equalsCards(
                    gameCards().mapIndexed { index, card ->
                        if (index == 0 || index == 1) card.copy(isFlipped = true) else card
                    }
                )
            )

            viewModel.onEvent(GameEvent.CardMismatched)
            advanceUntilIdle()

            val emission4 = awaitItem()
            assertTrue(emission4.cards.equalsCards(gameCards()))
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

            val reloadedState = awaitItem()
            assertTrue(reloadedState.isLoading.not())
            assertEquals(5, reloadedState.cards.size)
            assertEquals(500, reloadedState.score)
            assertNotEquals(gameCards(), reloadedState.cards)
        }
    }

    // AudioPlayer and AppPreferences stubs — prevent "no behavior defined" errors
    // since playShuffleEffect() accesses them as a side effect of card loading
    private fun stubAudio() {
        mocker.every { prefs.getBoolean(isAny(), isAny()) } returns false
        mocker.every { audioPlayer.playSound(isAny(), isAny()) } returns Unit
        mocker.every { audioPlayer.stopSound(isAny()) } returns Unit
    }

    // StateFlow replays its current value; on slow CI the IO coroutine may not
    // have finished yet so the first item is still the loading state — skip it.
    private suspend fun app.cash.turbine.ReceiveTurbine<GameState>.awaitLoaded(): GameState =
        awaitItem().let { if (it.isLoading) awaitItem() else it }

    private data class CardState(val astorCard: AstorCard, val isFlipped: Boolean, val isMatched: Boolean)

    private fun GameCard.toCardState() = CardState(astorCard, isFlipped, isMatched)

    private fun List<GameCard>.equalsCards(other: List<GameCard>) =
        size == other.size && zip(other).all { (a, b) -> a.toCardState() == b.toCardState() }

    private fun astorCards() = listOf(
        AstorCard("1", 1, "https://astorapi.co/api/v2/astor/1", ByteArray(0), "Astorbasaur"),
        AstorCard("2", 2, "https://astorapi.co/api/v2/astor/2", ByteArray(0), "Astorsaur"),
        AstorCard("3", 3, "https://astorapi.co/api/v2/astor/3", ByteArray(0), "Astorusaur"),
        AstorCard("4", 4, "https://astorapi.co/api/v2/astor/4", ByteArray(0), "Astormander"),
        AstorCard("5", 5, "https://astorapi.co/api/v2/astor/5", ByteArray(0), "Astormeleon"),
    )

    private fun gameCards() = astorCards().map { GameCard(astorCard = it) }
}
