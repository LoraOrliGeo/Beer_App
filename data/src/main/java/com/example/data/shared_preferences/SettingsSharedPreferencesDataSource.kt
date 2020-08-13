package com.example.data.shared_preferences

import android.content.Context

class SettingsSharedPreferencesDataSource(context: Context?, ownerId: String) {

    val settings = context?.getSharedPreferences("Settings" + ownerId, Context.MODE_PRIVATE)

    fun saveSettings(notifications: Boolean?, randomBeerSelection: Boolean?, darkTheme: Boolean?){
        val editor = settings?.edit()
        if(notifications != null) {
            editor?.putBoolean("notifications", notifications)
        }
        if(randomBeerSelection != null){
            editor?.putBoolean("randomBeerFavorites", randomBeerSelection)
        }
        if(darkTheme != null){
            editor?.putBoolean("darkTheme", darkTheme)
        }
        editor?.apply()
    }

    fun getNotifications() = settings?.getBoolean("notifications", false)

    fun getRandomBeerFavorites() = settings?.getBoolean("randomBeerFavorites", false)

    fun getDarkTheme() = settings?.getBoolean("darkTheme", false)
}