package io.lb.astormemory.game.platform.preferences

import android.content.Context
import android.content.SharedPreferences
import io.lb.astormemory.game.platform.preferences.AppPreferences

class AndroidAppPreferences(context: Context) : AppPreferences {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "astor_memory_prefs",
        Context.MODE_PRIVATE
    )

    override fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}
