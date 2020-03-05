package com.jans.societyoo.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jans.societyoo.R
import com.jans.societyoo.ui.MainActivity
import com.jans.societyoo.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private val activityLaunchRunnable = Runnable { callNextActivity() }

    private fun callNextActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor=resources.getColor(R.color.colorPrimaryDark)
            window.navigationBarColor=resources.getColor(R.color.colorPrimaryDark)
        }*/

        tvWelcome_splash.text =resources.getString(R.string.welcome_splash, resources.getString(R.string.app_name))
        tvContinue_splash.setOnClickListener {
            //handler.postDelayed(activityLaunchRunnable, 100)
            callNextActivity()
        }

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            callNextActivity()
        }

    }
}
