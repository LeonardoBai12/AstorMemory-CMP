package io.lb.astormemory.game.platform.preferences

import androidx.datastore.preferences.core.emptyPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import kotlinx.coroutines.test.runTest
import platform.Foundation.NSUserDefaults
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val SUITE_NAME = "io.lb.astormemory.UserDefaultsMigrationTest"

internal class UserDefaultsMigrationTest {
    lateinit var userDefaults: NSUserDefaults
    lateinit var migration: UserDefaultsMigration

    @BeforeTest
    fun setUp() {
        NSUserDefaults().removePersistentDomainForName(SUITE_NAME)
        userDefaults = NSUserDefaults(suiteName = SUITE_NAME)
        migration = UserDefaultsMigration(userDefaults)
    }

    @AfterTest
    fun tearDown() {
        NSUserDefaults().removePersistentDomainForName(SUITE_NAME)
    }

    @Test
    fun `When nothing was saved expect no migration`() = runTest {
        assertFalse(migration.shouldMigrate(emptyPreferences()))
    }

    @Test
    fun `When settings were saved expect them to be migrated`() = runTest {
        userDefaults.setBool(false, PreferencesKeys.IS_DARK_MODE.name)
        userDefaults.setBool(true, PreferencesKeys.IS_MUTED.name)
        userDefaults.setInteger(5, PreferencesKeys.CARDS_PER_LINE.name)
        userDefaults.setInteger(12, PreferencesKeys.LAST_AMOUNT.name)

        assertTrue(migration.shouldMigrate(emptyPreferences()))
        val migrated = migration.migrate(emptyPreferences())

        assertEquals(false, migrated[PreferencesKeys.IS_DARK_MODE])
        assertEquals(true, migrated[PreferencesKeys.IS_MUTED])
        assertEquals(5, migrated[PreferencesKeys.CARDS_PER_LINE])
        assertEquals(12, migrated[PreferencesKeys.LAST_AMOUNT])
    }

    @Test
    fun `When a setting was never saved expect it to stay missing`() = runTest {
        userDefaults.setBool(true, PreferencesKeys.IS_MUTED.name)

        val migrated = migration.migrate(emptyPreferences())

        assertNull(migrated[PreferencesKeys.IS_DARK_MODE])
        assertNull(migrated[PreferencesKeys.CARDS_PER_COLUMN])
    }

    @Test
    fun `When cleaned up expect the old settings to be removed`() = runTest {
        userDefaults.setBool(true, PreferencesKeys.IS_MUTED.name)
        userDefaults.setInteger(12, PreferencesKeys.LAST_AMOUNT.name)

        migration.cleanUp()

        assertNull(userDefaults.objectForKey(PreferencesKeys.IS_MUTED.name))
        assertNull(userDefaults.objectForKey(PreferencesKeys.LAST_AMOUNT.name))
        assertFalse(migration.shouldMigrate(emptyPreferences()))
    }
}
