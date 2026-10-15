package io.lb.astormemory.settings

import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@UsesMocks(SFXAndMusicRepository::class)
internal class SettingsViewModelTest {
    val mocker = Mocker()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()
    lateinit var viewModel: SettingsViewModel

    @BeforeTest
    fun setUp() {
        mocker.reset()
        viewModel = SettingsViewModel(sfxAndMusicRepository)
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When OnOptionChanged expect flip sound effect to be played`() {
        mocker.every { sfxAndMusicRepository.playCardFlippedSoundEffect() } returns Unit

        viewModel.onEvent(SettingsEvent.OnOptionChanged)

        mocker.verify { sfxAndMusicRepository.playCardFlippedSoundEffect() }
    }
}
