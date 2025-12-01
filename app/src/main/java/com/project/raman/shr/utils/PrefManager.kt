package com.project.raman.shr.utils

import android.content.Context
import android.content.SharedPreferences

object PrefKeys {
    const val USER_PROFILE_NAME = "user_profile_name"
    const val USER_PROFILE_URL = "user_profile_url"
    const val UER_EMAIL = "user_email"
}

object PrefManager {

    private const val PREF_NAME = "app_prefs"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ----------- Put Methods -----------
    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    // ----------- Get Methods -----------
    fun getString(key: String, default: String? = null): String {
        return prefs.getString(key, default)?:""
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    fun getInt(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    fun getLong(key: String, default: Long = 0L): Long {
        return prefs.getLong(key, default)
    }

    // ----------- Clear -----------
    fun clear() {
        prefs.edit().clear().apply()
    }
}

