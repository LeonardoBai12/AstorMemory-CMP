package io.lb.astormemory.settings

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
internal class SettingsViewModelTest {
    val mocker = Mocker()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()
    val settingsRepository: SettingsRepository = mocker.mock()
    lateinit var viewModel: SettingsViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        viewModel = SettingsViewModel(sfxAndMusicRepository, settingsRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When OnDarkModeChanged expect flip sound and dark mode to be saved`() = runTest {
        stubRepositories()

        viewModel.onEvent(SettingsEvent.OnDarkModeChanged(isDarkMode = true))

        mocker.verifyWithSuspend {
            sfxAndMusicRepository.playCardFlippedSoundEffect()
            settingsRepository.setDarkMode(true)
        }
    }

    @Test
    fun `When OnDynamicLayoutChanged expect flip sound and dynamic layout to be saved`() = runTest {
        stubRepositories()

        viewModel.onEvent(SettingsEvent.OnDynamicLayoutChanged(isDynamicLayout = false))

        mocker.verifyWithSuspend {
            sfxAndMusicRepository.playCardFlippedSoundEffect()
            settingsRepository.setDynamicLayout(false)
        }
    }

    @Test
    fun `When OnSoundEffectsEnabledChanged expect setting to be saved before the flip sound`() = runTest {
        stubRepositories()

        viewModel.onEvent(SettingsEvent.OnSoundEffectsEnabledChanged(isSoundEffectsEnabled = true))

        mocker.verifyWithSuspend {
            settingsRepository.setSoundEffectsEnabled(true)
            sfxAndMusicRepository.playCardFlippedSoundEffect()
        }
    }

    @Test
    fun `When OnCardsPerLineChanged expect cards per line to be saved without sound`() = runTest {
        stubRepositories()

        viewModel.onEvent(SettingsEvent.OnCardsPerLineChanged(cardsPerLine = 5))

        mocker.verifyWithSuspend { settingsRepository.setCardsPerLine(5) }
    }

    @Test
    fun `When OnCardsPerColumnChanged expect cards per column to be saved without sound`() = runTest {
        stubRepositories()

        viewModel.onEvent(SettingsEvent.OnCardsPerColumnChanged(cardsPerColumn = 8))

        mocker.verifyWithSuspend { settingsRepository.setCardsPerColumn(8) }
    }

    private suspend fun stubRepositories() {
        mocker.everySuspending { sfxAndMusicRepository.playCardFlippedSoundEffect() } returns Unit
        mocker.everySuspending { settingsRepository.setDarkMode(isAny()) } returns Unit
        mocker.everySuspending { settingsRepository.setDynamicLayout(isAny()) } returns Unit
        mocker.everySuspending { settingsRepository.setSoundEffectsEnabled(isAny()) } returns Unit
        mocker.everySuspending { settingsRepository.setCardsPerLine(isAny()) } returns Unit
        mocker.everySuspending { settingsRepository.setCardsPerColumn(isAny()) } returns Unit
    }
}
