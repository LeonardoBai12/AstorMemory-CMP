package io.lb.astormemory.game.platform.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal const val SETTINGS_DATA_STORE_NAME = "astor_memory_settings"

/**
 * Creates the [DataStore] where the settings chosen by the user are saved, migrating the
 * settings saved by previous versions of the app.
 */
expect class SettingsDataStoreFactory {
    fun create(): DataStore<Preferences>
}
