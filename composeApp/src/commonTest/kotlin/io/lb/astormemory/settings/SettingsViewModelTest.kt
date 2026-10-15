package io.lb.astormemory.settings

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
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
        mocker.reset()
        mocker.every { sfxAndMusicRepository.playCardFlippedSoundEffect() } returns Unit
        mocker.every { settingsRepository.setDarkMode(isAny()) } returns Unit
        mocker.every { settingsRepository.setDynamicLayout(isAny()) } returns Unit
        mocker.every { settingsRepository.setSoundEffectsEnabled(isAny()) } returns Unit
        mocker.every { settingsRepository.setCardsPerLine(isAny()) } returns Unit
        mocker.every { settingsRepository.setCardsPerColumn(isAny()) } returns Unit
        viewModel = SettingsViewModel(sfxAndMusicRepository, settingsRepository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When OnDarkModeChanged expect flip sound and dark mode to be saved`() {
        viewModel.onEvent(SettingsEvent.OnDarkModeChanged(isDarkMode = true))

        mocker.verify {
            sfxAndMusicRepository.playCardFlippedSoundEffect()
            settingsRepository.setDarkMode(true)
        }
    }

    @Test
    fun `When OnDynamicLayoutChanged expect flip sound and dynamic layout to be saved`() {
        viewModel.onEvent(SettingsEvent.OnDynamicLayoutChanged(isDynamicLayout = false))

        mocker.verify {
            sfxAndMusicRepository.playCardFlippedSoundEffect()
            settingsRepository.setDynamicLayout(false)
        }
    }

    @Test
    fun `When OnSoundEffectsEnabledChanged expect setting to be saved before the flip sound`() {
        viewModel.onEvent(SettingsEvent.OnSoundEffectsEnabledChanged(isSoundEffectsEnabled = true))

        mocker.verify {
            settingsRepository.setSoundEffectsEnabled(true)
            sfxAndMusicRepository.playCardFlippedSoundEffect()
        }
    }

    @Test
    fun `When OnCardsPerLineChanged expect cards per line to be saved without sound`() {
        viewModel.onEvent(SettingsEvent.OnCardsPerLineChanged(cardsPerLine = 5))

        mocker.verify { settingsRepository.setCardsPerLine(5) }
    }

    @Test
    fun `When OnCardsPerColumnChanged expect cards per column to be saved without sound`() {
        viewModel.onEvent(SettingsEvent.OnCardsPerColumnChanged(cardsPerColumn = 8))

        mocker.verify { settingsRepository.setCardsPerColumn(8) }
    }
}
