package com.carloshns.mychronometer.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.carloshns.mychronometer.R
import com.carloshns.mychronometer.ui.chronometer.ChronometerActivity
import com.carloshns.mychronometer.ui.main.MainActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val intent2 = Intent(this, ChronometerActivity::class.java)
            startActivity(intent)
            finish()
        }, 0)

    }
}