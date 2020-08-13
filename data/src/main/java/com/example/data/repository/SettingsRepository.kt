package com.example.data.repository

import android.content.Context
import com.example.data.shared_preferences.SettingsSharedPreferencesDataSource

class SettingsRepository(context: Context?, ownerId: String) {

    private val settingsSharedPreferences =
        SettingsSharedPreferencesDataSource(context, ownerId)

    fun saveSettings(notifications: Boolean?, randomBeerSelection: Boolean?, darkTheme: Boolean?) {
        settingsSharedPreferences.saveSettings(notifications, randomBeerSelection, darkTheme)
    }

    fun getNotifications(): Boolean {
        return settingsSharedPreferences.getNotifications() ?: false
    }

    fun getRandomBeerFavorites(): Boolean{
        return settingsSharedPreferences.getRandomBeerFavorites() ?: false
    }

    fun getDarkTheme(): Boolean {
        return settingsSharedPreferences.getDarkTheme() ?: false
    }
}