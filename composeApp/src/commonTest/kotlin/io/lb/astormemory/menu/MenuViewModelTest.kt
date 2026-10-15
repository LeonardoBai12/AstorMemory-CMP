package io.lb.astormemory.menu

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(SFXAndMusicRepository::class, SettingsRepository::class)
internal class MenuViewModelTest {
    val mocker = Mocker()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()
    val settingsRepository: SettingsRepository = mocker.mock()
    lateinit var viewModel: MenuViewModel

    @BeforeTest
    fun setUp() {
        mocker.reset()
        viewModel = MenuViewModel(sfxAndMusicRepository, settingsRepository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When OnStart expect menu song to be played`() {
        mocker.every { sfxAndMusicRepository.playMenuSong() } returns Unit

        viewModel.onEvent(MenuEvent.OnStart)

        mocker.verify { sfxAndMusicRepository.playMenuSong() }
    }

    @Test
    fun `When OnButtonClicked expect click sound effect to be played`() {
        mocker.every { sfxAndMusicRepository.playButtonClickedSoundEffect() } returns Unit

        viewModel.onEvent(MenuEvent.OnButtonClicked)

        mocker.verify { sfxAndMusicRepository.playButtonClickedSoundEffect() }
    }

    @Test
    fun `When OnMutedChanged expect muted to be saved`() {
        mocker.every { settingsRepository.setMuted(isAny()) } returns Unit

        viewModel.onEvent(MenuEvent.OnMutedChanged(isMuted = true))

        mocker.verify { settingsRepository.setMuted(true) }
    }

    @Test
    fun `When OnAmountChanged expect last amount to be saved`() {
        mocker.every { settingsRepository.setLastAmount(isAny()) } returns Unit

        viewModel.onEvent(MenuEvent.OnAmountChanged(amount = 12))

        mocker.verify { settingsRepository.setLastAmount(12) }
    }
}
