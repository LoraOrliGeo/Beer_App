package com.example.data.shared_preferences

import android.content.Context

class SharedPreferencesDataSource(context: Context?, ownerId: String) {

    val filters = context?.getSharedPreferences("Filters" + ownerId, Context.MODE_PRIVATE)

    fun getString(key: String): String? {
        return filters?.getString(key, "")
    }

    fun putString(key: String, filter: String?) {
        val editor = filters?.edit()
        editor?.putString(key, filter)
        editor?.apply()
    }

    fun getFloat(key: String): Float? {
        return filters?.getFloat(key, 0.0F)
    }

    fun putFloat(key: String, filter: Float?) {
        val editor = filters?.edit()
        editor?.putFloat(key, filter ?: 0F)
        editor?.apply()
    }

    fun getLong(key: String): Long? {
        return filters?.getLong(key, 0)
    }

    fun putLong(key: String, filter: Long?) {
        val editor = filters?.edit()
        editor?.putLong(key, filter ?: 0)
        editor?.apply()
    }

    fun clearAll() {
        val editor = filters?.edit()
        editor?.clear()
        editor?.apply()
    }
}