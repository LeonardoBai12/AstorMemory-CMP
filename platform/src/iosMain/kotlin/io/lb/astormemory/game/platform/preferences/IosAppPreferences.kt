package io.lb.astormemory.game.platform.preferences

import io.lb.astormemory.game.platform.preferences.AppPreferences
import platform.Foundation.NSUserDefaults

class IosAppPreferences : AppPreferences {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        val value = userDefaults.integerForKey(key)
        return if (value == 0L && userDefaults.objectForKey(key) == null) {
            defaultValue
        } else {
            value.toInt()
        }
    }

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) == null) {
            defaultValue
        } else {
            userDefaults.boolForKey(key)
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }

    override fun clear() {
        userDefaults.dictionaryRepresentation().keys.forEach { key ->
            userDefaults.removeObjectForKey(key.toString())
        }
    }
}
