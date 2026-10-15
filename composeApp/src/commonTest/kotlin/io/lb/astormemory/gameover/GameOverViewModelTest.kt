package io.lb.astormemory.gameover

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(SFXAndMusicRepository::class)
internal class GameOverViewModelTest {
    val mocker = Mocker()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When OnStart expect game over song to be played with the game result`() = runTest {
        stubRepositories()

        val viewModel = GameOverViewModel(sfxAndMusicRepository, score = 1500, amount = 12)

        viewModel.onEvent(GameOverEvent.OnStart)

        mocker.verifyWithSuspend { sfxAndMusicRepository.playGameOverSong(1500, 12) }
    }

    @Test
    fun `When OnStart with no score expect game over song to be played with zero score`() = runTest {
        stubRepositories()

        val viewModel = GameOverViewModel(sfxAndMusicRepository, score = 0, amount = 5)

        viewModel.onEvent(GameOverEvent.OnStart)

        mocker.verifyWithSuspend { sfxAndMusicRepository.playGameOverSong(0, 5) }
    }

    private suspend fun stubRepositories() {
        mocker.everySuspending { sfxAndMusicRepository.playGameOverSong(isAny(), isAny()) } returns Unit
    }
}
