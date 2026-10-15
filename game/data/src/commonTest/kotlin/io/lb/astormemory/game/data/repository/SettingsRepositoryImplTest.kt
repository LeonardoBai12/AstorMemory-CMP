package io.lb.astormemory.game.data.repository

import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.shared.model.AppSettings
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.mock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@UsesMocks(AppPreferences::class)
class SettingsRepositoryImplTest {
    val mocker = Mocker()
    val prefs: AppPreferences = mocker.mock()

    @BeforeTest
    fun setUp() {
        mocker.reset()
        mocker.every { prefs.putBoolean(isAny(), isAny()) } returns Unit
        mocker.every { prefs.putInt(isAny(), isAny()) } returns Unit
    }

    @AfterTest
    fun tearDown() = mocker.reset()

    @Test
    fun `When nothing was saved expect default settings`() {
        stubNothingSaved()

        val repository = SettingsRepositoryImpl(prefs)

        assertEquals(AppSettings(), repository.settings.value)
    }

    @Test
    fun `When dark mode was never saved expect it to follow the system theme`() {
        stubNothingSaved()

        val repository = SettingsRepositoryImpl(prefs)

        assertNull(repository.settings.value.isDarkMode)
    }

    @Test
    fun `When settings were saved expect them to be read`() {
        mocker.every { prefs.contains(PreferencesKeys.IS_DARK_MODE) } returns true
        mocker.every { prefs.getBoolean(isEqual(PreferencesKeys.IS_DARK_MODE), isAny()) } returns true
        mocker.every { prefs.getBoolean(isEqual(PreferencesKeys.IS_MUTED), isAny()) } returns true
        mocker.every { prefs.getBoolean(isEqual(PreferencesKeys.IS_DYNAMIC_LAYOUT), isAny()) } returns false
        mocker.every {
            prefs.getBoolean(isEqual(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED), isAny())
        } returns false
        mocker.every { prefs.getInt(isEqual(PreferencesKeys.CARDS_PER_LINE), isAny()) } returns 5
        mocker.every { prefs.getInt(isEqual(PreferencesKeys.CARDS_PER_COLUMN), isAny()) } returns 7
        mocker.every { prefs.getInt(isEqual(PreferencesKeys.LAST_AMOUNT), isAny()) } returns 12

        val repository = SettingsRepositoryImpl(prefs)

        val expected = AppSettings(
            isDarkMode = true,
            isMuted = true,
            isDynamicLayout = false,
            isSoundEffectsEnabled = false,
            cardsPerLine = 5,
            cardsPerColumn = 7,
            lastAmount = 12
        )
        assertEquals(expected, repository.settings.value)
    }

    @Test
    fun `When set dark mode expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setDarkMode(true)

        mocker.verify(exhaustive = false) { prefs.putBoolean(PreferencesKeys.IS_DARK_MODE, true) }
        assertEquals(true, repository.settings.value.isDarkMode)
    }

    @Test
    fun `When set muted expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setMuted(true)

        mocker.verify(exhaustive = false) { prefs.putBoolean(PreferencesKeys.IS_MUTED, true) }
        assertEquals(true, repository.settings.value.isMuted)
    }

    @Test
    fun `When set dynamic layout expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setDynamicLayout(false)

        mocker.verify(exhaustive = false) {
            prefs.putBoolean(PreferencesKeys.IS_DYNAMIC_LAYOUT, false)
        }
        assertEquals(false, repository.settings.value.isDynamicLayout)
    }

    @Test
    fun `When set sound effects enabled expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setSoundEffectsEnabled(false)

        mocker.verify(exhaustive = false) {
            prefs.putBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, false)
        }
        assertEquals(false, repository.settings.value.isSoundEffectsEnabled)
    }

    @Test
    fun `When set cards per line expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setCardsPerLine(5)

        mocker.verify(exhaustive = false) { prefs.putInt(PreferencesKeys.CARDS_PER_LINE, 5) }
        assertEquals(5, repository.settings.value.cardsPerLine)
    }

    @Test
    fun `When set cards per column expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setCardsPerColumn(8)

        mocker.verify(exhaustive = false) { prefs.putInt(PreferencesKeys.CARDS_PER_COLUMN, 8) }
        assertEquals(8, repository.settings.value.cardsPerColumn)
    }

    @Test
    fun `When set last amount expect it to be saved and emitted`() {
        stubNothingSaved()
        val repository = SettingsRepositoryImpl(prefs)

        repository.setLastAmount(15)

        mocker.verify(exhaustive = false) { prefs.putInt(PreferencesKeys.LAST_AMOUNT, 15) }
        assertEquals(15, repository.settings.value.lastAmount)
    }

    private fun stubNothingSaved() {
        mocker.every { prefs.contains(isAny()) } returns false
        mocker.every { prefs.getBoolean(isAny(), isAny()) } runs { args -> args[1] as Boolean }
        mocker.every { prefs.getInt(isAny(), isAny()) } runs { args -> args[1] as Int }
    }
}
