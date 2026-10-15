package io.lb.astormemory.game.data.fake

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory [DataStore] for tests, so they don't depend on files.
 */
class FakePreferencesDataStore(
    initial: Preferences = emptyPreferences()
) : DataStore<Preferences> {
    private val state = MutableStateFlow(initial)

    override val data: Flow<Preferences> = state

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val updated = transform(state.value)
        state.value = updated
        return updated
    }

    /**
     * Save a value without suspending, to set up a test.
     */
    operator fun <T> set(key: Preferences.Key<T>, value: T) {
        state.value = state.value.toMutablePreferences().apply { this[key] = value }.toPreferences()
    }

    /**
     * The value currently saved for the given key.
     */
    operator fun <T> get(key: Preferences.Key<T>): T? = state.value[key]
}
