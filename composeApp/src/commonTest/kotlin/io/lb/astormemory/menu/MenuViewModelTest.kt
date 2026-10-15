package io.lb.astormemory.menu

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
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

@UsesMocks(SFXAndMusicRepository::class, SettingsRepository::class)
internal class MenuViewModelTest {
    val mocker = Mocker()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()
    val settingsRepository: SettingsRepository = mocker.mock()
    lateinit var viewModel: MenuViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        viewModel = MenuViewModel(sfxAndMusicRepository, settingsRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When OnStart expect menu song to be played`() = runTest {
        mocker.everySuspending { sfxAndMusicRepository.playMenuSong() } returns Unit

        viewModel.onEvent(MenuEvent.OnStart)

        mocker.verifyWithSuspend { sfxAndMusicRepository.playMenuSong() }
    }

    @Test
    fun `When OnButtonClicked expect click sound effect to be played`() = runTest {
        mocker.everySuspending { sfxAndMusicRepository.playButtonClickedSoundEffect() } returns Unit

        viewModel.onEvent(MenuEvent.OnButtonClicked)

        mocker.verifyWithSuspend { sfxAndMusicRepository.playButtonClickedSoundEffect() }
    }

    @Test
    fun `When OnMutedChanged expect muted to be saved before the click sound`() = runTest {
        mocker.everySuspending { settingsRepository.setMuted(isAny()) } returns Unit
        mocker.everySuspending { sfxAndMusicRepository.playButtonClickedSoundEffect() } returns Unit

        viewModel.onEvent(MenuEvent.OnMutedChanged(isMuted = true))

        mocker.verifyWithSuspend {
            settingsRepository.setMuted(true)
            sfxAndMusicRepository.playButtonClickedSoundEffect()
        }
    }

    @Test
    fun `When OnAmountChanged expect last amount to be saved`() = runTest {
        mocker.everySuspending { settingsRepository.setLastAmount(isAny()) } returns Unit

        viewModel.onEvent(MenuEvent.OnAmountChanged(amount = 12))

        mocker.verifyWithSuspend { settingsRepository.setLastAmount(12) }
    }
}
