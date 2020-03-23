package com.jans.societyoo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.ui.dashboard.DashboardActivity
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.ui.onboard.OnBoardActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private val activityLaunchRunnable = Runnable { callNextActivity() }
    var preferences = UserPreferences(this)
    private fun callNextActivity() {
        if (preferences.appOpenFirstTime!!) {
            if (TextUtils.isEmpty(preferences.mobileNum)) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        } else{
            startActivity(Intent(this, OnBoardActivity::class.java))
        }
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

        tvWelcome_splash.text =
            resources.getString(R.string.welcome_splash, resources.getString(R.string.app_name))
        tvContinue_splash.setOnClickListener {
            //handler.postDelayed(activityLaunchRunnable, 100)
            callNextActivity()
        }

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            callNextActivity()
        }
        //handler.postDelayed(activityLaunchRunnable, 2000)

    }
}
