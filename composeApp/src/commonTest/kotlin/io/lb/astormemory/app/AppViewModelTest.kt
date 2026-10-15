package io.lb.astormemory.app

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.domain.repository.SettingsRepository
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@UsesMocks(SettingsRepository::class, SFXAndMusicRepository::class)
internal class AppViewModelTest {
    val mocker = Mocker()
    val settingsRepository: SettingsRepository = mocker.mock()
    val sfxAndMusicRepository: SFXAndMusicRepository = mocker.mock()
    val settings = MutableStateFlow(AppSettings())

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mocker.reset()
        settings.value = AppSettings()
        mocker.every { settingsRepository.settings } returns settings
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mocker.reset()
    }

    @Test
    fun `When the view model is created expect the saved settings as state`() {
        settings.value = AppSettings(
            isDarkMode = true,
            isMuted = true,
            isDynamicLayout = false,
            isSoundEffectsEnabled = false,
            cardsPerLine = 5,
            cardsPerColumn = 7,
            lastAmount = 12
        )

        val viewModel = AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = false)

        val expected = AppState(
            isDarkMode = true,
            isMuted = true,
            isDynamicLayout = false,
            isSoundEffectsEnabled = false,
            cardsPerLine = 5,
            cardsPerColumn = 7,
            initialAmount = 12
        )
        assertEquals(expected, viewModel.state.value)
    }

    @Test
    fun `When dark mode was never chosen expect the system theme to be used`() {
        settings.value = AppSettings(isDarkMode = null)

        val darkSystem = AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = true)
        val lightSystem = AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = false)

        assertTrue(darkSystem.state.value.isDarkMode)
        assertFalse(lightSystem.state.value.isDarkMode)
    }

    @Test
    fun `When dark mode was chosen expect it to override the system theme`() {
        settings.value = AppSettings(isDarkMode = false)

        val viewModel = AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = true)

        assertFalse(viewModel.state.value.isDarkMode)
    }

    @Test
    fun `When the settings change expect the state to be updated`() {
        val viewModel = AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = false)

        settings.value = settings.value.copy(isMuted = true, lastAmount = 20)

        assertTrue(viewModel.state.value.isMuted)
        assertEquals(20, viewModel.state.value.initialAmount)
    }

    @Test
    fun `When the view model is cleared expect songs and sound effects to be released`() {
        mocker.every { sfxAndMusicRepository.release() } returns Unit
        val store = ViewModelStore()
        ViewModelProvider.create(
            store = store,
            factory = viewModelFactory {
                initializer {
                    AppViewModel(settingsRepository, sfxAndMusicRepository, isSystemInDarkMode = false)
                }
            }
        )[AppViewModel::class]

        store.clear()

        mocker.verify(exhaustive = false) { sfxAndMusicRepository.release() }
    }
}
