package io.lb.astormemory.game.platform.preferences

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import platform.Foundation.NSUserDefaults

/**
 * Moves the settings saved in [NSUserDefaults] by previous versions of the app to the DataStore.
 * Keys that were never saved stay missing, so their defaults keep applying.
 */
internal class UserDefaultsMigration(
    private val userDefaults: NSUserDefaults
) : DataMigration<Preferences> {

    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        return allKeyNames().any { userDefaults.objectForKey(it) != null }
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val preferences = currentData.toMutablePreferences()
        PreferencesKeys.BOOLEAN_KEYS.forEach { key ->
            if (userDefaults.objectForKey(key.name) != null) {
                preferences[key] = userDefaults.boolForKey(key.name)
            }
        }
        PreferencesKeys.INT_KEYS.forEach { key ->
            if (userDefaults.objectForKey(key.name) != null) {
                preferences[key] = userDefaults.integerForKey(key.name).toInt()
            }
        }
        return preferences.toPreferences()
    }

    override suspend fun cleanUp() {
        allKeyNames().forEach { userDefaults.removeObjectForKey(it) }
    }

    private fun allKeyNames() = (PreferencesKeys.BOOLEAN_KEYS + PreferencesKeys.INT_KEYS).map { it.name }
}
