package com.example.beerapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.beerapp.R
import com.example.beerapp.databinding.ActivityMainBinding
import com.example.beerapp.presenters.work.NotificationsWorker
import com.example.beerapp.presenters.work.WORK_TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import java.util.concurrent.TimeUnit

class ActivityMain : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        sharedPreferences = getSharedPreferences("Settings" + account?.id, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("darkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = this.findNavController(R.id.mainNavHost)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainNavHost)
        return navController.navigateUp()
    }

    override fun onStop() {
        super.onStop()

        if(sharedPreferences.getBoolean("notifications", false)) {
            val notificationWork = OneTimeWorkRequest.Builder(NotificationsWorker::class.java)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance().enqueue(notificationWork)
        }
    }
}
