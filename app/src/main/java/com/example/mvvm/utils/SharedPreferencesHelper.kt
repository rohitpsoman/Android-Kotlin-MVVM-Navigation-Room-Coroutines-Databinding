package com.example.mvvm.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

/**
 * A helper class for handling SharedPreferences transactions
 */

class SharedPreferencesHelper constructor(var mContext: Context) {

    private val preferences: SharedPreferences by lazy {
        PreferenceManager
            .getDefaultSharedPreferences(mContext)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit { putInt(key, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit { putBoolean(key, value) }
    }

    fun putString(key: String, value: String?) {
        preferences.edit { putString(key, value) }
    }

    fun putFloat(key: String, value: Float) {
        preferences.edit { putFloat(key, value) }
    }

    fun putLong(key: String, value: Long) {
        preferences.edit { putLong(key, value) }
    }

    fun getLong(key: String, _default: Long): Long {
        return preferences.getLong(key, _default)
    }

    fun getFloat(key: String, _default: Float): Float {
        return preferences.getFloat(key, _default)
    }

    fun getString(key: String, _default: String): String {
        return preferences.getString(key, _default)
    }

    fun getInt(key: String, _default: Int): Int {
        return preferences.getInt(key, _default)
    }

    fun getBoolean(key: String, _default: Boolean): Boolean {
        return preferences.getBoolean(key, _default)
    }

    fun clearPreferences() {
        preferences.edit().clear().apply()
    }
}