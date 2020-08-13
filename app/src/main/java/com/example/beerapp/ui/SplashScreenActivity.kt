package com.example.beerapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.beerapp.R
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 2000L

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        activityScope.launch {
            delay(SPLASH_TIME_OUT)

            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
