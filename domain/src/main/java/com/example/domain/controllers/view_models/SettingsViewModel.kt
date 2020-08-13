package com.example.domain.controllers.view_models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.data.repository.SettingsRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class SettingsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val account = GoogleSignIn.getLastSignedInAccount(application)
    private val settingsRepository = SettingsRepository(application, account?.id ?: "")

    fun saveSettings(notifications: Boolean?, randomBeerSelection: Boolean?, darkTheme: Boolean?) {
        settingsRepository.saveSettings(notifications, randomBeerSelection, darkTheme)
    }

    fun getNotifications(): Boolean {
        return settingsRepository.getNotifications()
    }

    fun getRandomBeerFavortes(): Boolean {
        return settingsRepository.getRandomBeerFavorites()
    }

    fun getDarkTheme(): Boolean {
        return settingsRepository.getDarkTheme()
    }

    fun getUser() = account

    fun signOut(context: Context?, navigate: () -> Unit) {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build()

        if (context != null) {
            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            mGoogleSignInClient.signOut().addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    try {
                        task.getResult(ApiException::class.java)
                        navigate()
                    } catch (e: ApiException) {
                        Log.w("SettingsFragment", "signOutResult:failed code=" + e.statusCode)
                    }
                }
            })
        }
    }
}