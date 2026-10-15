package io.lb.astormemory.game.data.repository

import app.cash.turbine.test
import io.lb.astormemory.game.data.datasource.SettingsDataSource
import io.lb.astormemory.game.data.fake.FakePreferencesDataStore
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// SettingsDataSource is a concrete class and cannot be mocked by MockMP (which only mocks
// interfaces). A real SettingsDataSource is constructed around an in-memory DataStore instead.
class SettingsRepositoryImplTest {
    lateinit var dataStore: FakePreferencesDataStore
    lateinit var repository: SettingsRepositoryImpl

    @BeforeTest
    fun setUp() {
        dataStore = FakePreferencesDataStore()
        repository = SettingsRepositoryImpl(SettingsDataSource(dataStore))
    }

    @Test
    fun `When settings change expect them to be emitted`() = runTest {
        repository.settings.test {
            assertEquals(AppSettings(), awaitItem())

            repository.setLastAmount(12)

            assertEquals(AppSettings(lastAmount = 12), awaitItem())
        }
    }

    @Test
    fun `When set dark mode expect it to be saved`() = runTest {
        repository.setDarkMode(true)

        assertEquals(true, dataStore[PreferencesKeys.IS_DARK_MODE])
    }

    @Test
    fun `When set muted expect it to be saved`() = runTest {
        repository.setMuted(true)

        assertEquals(true, dataStore[PreferencesKeys.IS_MUTED])
    }

    @Test
    fun `When set dynamic layout expect it to be saved`() = runTest {
        repository.setDynamicLayout(false)

        assertEquals(false, dataStore[PreferencesKeys.IS_DYNAMIC_LAYOUT])
    }

    @Test
    fun `When set sound effects enabled expect it to be saved`() = runTest {
        repository.setSoundEffectsEnabled(false)

        assertEquals(false, dataStore[PreferencesKeys.IS_SOUND_EFFECTS_ENABLED])
    }

    @Test
    fun `When set cards per line expect it to be saved`() = runTest {
        repository.setCardsPerLine(5)

        assertEquals(5, dataStore[PreferencesKeys.CARDS_PER_LINE])
    }

    @Test
    fun `When set cards per column expect it to be saved`() = runTest {
        repository.setCardsPerColumn(8)

        assertEquals(8, dataStore[PreferencesKeys.CARDS_PER_COLUMN])
    }

    @Test
    fun `When set last amount expect it to be saved`() = runTest {
        repository.setLastAmount(15)

        assertEquals(15, dataStore[PreferencesKeys.LAST_AMOUNT])
    }
}
