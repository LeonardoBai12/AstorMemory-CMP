package io.lb.astormemory.game.platform.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile

private const val LEGACY_SHARED_PREFERENCES_NAME = "astor_memory_prefs"

actual class SettingsDataStoreFactory(private val context: Context) {
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            migrations = listOf(SharedPreferencesMigration(context, LEGACY_SHARED_PREFERENCES_NAME)),
            produceFile = { context.preferencesDataStoreFile(SETTINGS_DATA_STORE_NAME) }
        )
    }
}
