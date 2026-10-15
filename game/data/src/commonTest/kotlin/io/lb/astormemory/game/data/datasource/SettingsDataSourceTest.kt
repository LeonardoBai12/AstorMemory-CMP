package io.lb.astormemory.game.data.datasource

import app.cash.turbine.test
import io.lb.astormemory.game.data.fake.FakePreferencesDataStore
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.shared.model.AppSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SettingsDataSourceTest {
    lateinit var dataStore: FakePreferencesDataStore
    lateinit var dataSource: SettingsDataSource

    @BeforeTest
    fun setUp() {
        dataStore = FakePreferencesDataStore()
        dataSource = SettingsDataSource(dataStore)
    }

    @Test
    fun `When nothing was saved expect default settings`() = runTest {
        assertEquals(AppSettings(), dataSource.getSettings())
    }

    @Test
    fun `When dark mode was never saved expect it to follow the system theme`() = runTest {
        assertNull(dataSource.getSettings().isDarkMode)
    }

    @Test
    fun `When settings were saved expect them to be read`() = runTest {
        dataStore[PreferencesKeys.IS_DARK_MODE] = true
        dataStore[PreferencesKeys.IS_MUTED] = true
        dataStore[PreferencesKeys.IS_DYNAMIC_LAYOUT] = false
        dataStore[PreferencesKeys.IS_SOUND_EFFECTS_ENABLED] = false
        dataStore[PreferencesKeys.CARDS_PER_LINE] = 5
        dataStore[PreferencesKeys.CARDS_PER_COLUMN] = 7
        dataStore[PreferencesKeys.LAST_AMOUNT] = 12

        val expected = AppSettings(
            isDarkMode = true,
            isMuted = true,
            isDynamicLayout = false,
            isSoundEffectsEnabled = false,
            cardsPerLine = 5,
            cardsPerColumn = 7,
            lastAmount = 12
        )
        assertEquals(expected, dataSource.getSettings())
    }

    @Test
    fun `When a setting is saved expect the settings flow to emit it`() = runTest {
        dataSource.settings.test {
            assertEquals(AppSettings(), awaitItem())

            dataSource.setMuted(true)

            assertEquals(AppSettings(isMuted = true), awaitItem())
        }
    }

    @Test
    fun `When set dark mode expect it to be saved`() = runTest {
        dataSource.setDarkMode(false)

        assertEquals(false, dataStore[PreferencesKeys.IS_DARK_MODE])
        assertEquals(false, dataSource.getSettings().isDarkMode)
    }

    @Test
    fun `When set muted expect it to be saved`() = runTest {
        dataSource.setMuted(true)

        assertEquals(true, dataStore[PreferencesKeys.IS_MUTED])
    }

    @Test
    fun `When set dynamic layout expect it to be saved`() = runTest {
        dataSource.setDynamicLayout(false)

        assertEquals(false, dataStore[PreferencesKeys.IS_DYNAMIC_LAYOUT])
    }

    @Test
    fun `When set sound effects enabled expect it to be saved`() = runTest {
        dataSource.setSoundEffectsEnabled(false)

        assertEquals(false, dataStore[PreferencesKeys.IS_SOUND_EFFECTS_ENABLED])
    }

    @Test
    fun `When set cards per line expect it to be saved`() = runTest {
        dataSource.setCardsPerLine(5)

        assertEquals(5, dataStore[PreferencesKeys.CARDS_PER_LINE])
    }

    @Test
    fun `When set cards per column expect it to be saved`() = runTest {
        dataSource.setCardsPerColumn(8)

        assertEquals(8, dataStore[PreferencesKeys.CARDS_PER_COLUMN])
    }

    @Test
    fun `When set last amount expect it to be saved`() = runTest {
        dataSource.setLastAmount(15)

        assertEquals(15, dataStore[PreferencesKeys.LAST_AMOUNT])
    }
}
