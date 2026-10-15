package io.lb.astormemory.gameover

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
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
        mocker.reset()
        mocker.every { sfxAndMusicRepository.playGameOverSong(isAny(), isAny()) } returns Unit
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When OnStart expect game over song to be played with the game result`() {
        val viewModel = GameOverViewModel(sfxAndMusicRepository, score = 1500, amount = 12)

        viewModel.onEvent(GameOverEvent.OnStart)

        mocker.verify { sfxAndMusicRepository.playGameOverSong(1500, 12) }
    }

    @Test
    fun `When OnStart with no score expect game over song to be played with zero score`() {
        val viewModel = GameOverViewModel(sfxAndMusicRepository, score = 0, amount = 5)

        viewModel.onEvent(GameOverEvent.OnStart)

        mocker.verify { sfxAndMusicRepository.playGameOverSong(0, 5) }
    }
}
