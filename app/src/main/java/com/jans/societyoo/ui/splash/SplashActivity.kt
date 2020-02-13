package com.jans.societyoo.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.jans.societyoo.R
import com.jans.societyoo.ui.MainActivity
import com.jans.societyoo.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private val activityLaunchRunnable = Runnable { callNextActivity() }

    private fun callNextActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        fullscreen_content.setOnClickListener {

        }

        handler.postDelayed(activityLaunchRunnable, 2000)
    }
}
