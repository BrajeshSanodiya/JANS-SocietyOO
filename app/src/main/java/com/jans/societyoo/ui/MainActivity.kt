package com.jans.societyoo.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.tracking.EventName
import com.jans.tracking.PropertyName
import com.jans.tracking.Tracking
import com.jans.tracking.TrackingOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main)

        //val actionBar: ActionBar? = supportActionBar
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        supportActionBar!!.setLogo(R.drawable.header_logo);
        supportActionBar!!.setDisplayUseLogoEnabled(true);

        var preferences=UserPreferences(this)
        println("--------------- Pref ---------------")
        println(preferences.userName)
        UserPreferences::userName.set(preferences,"Brajesh");
        println(preferences.userName)

        println(""+preferences.emailAccount)
        UserPreferences::emailAccount.set(preferences,"sanodiya.brajesh@gmail.com");
        println(""+preferences.emailAccount)
        println(preferences.userName)

        // Tracking
        val trackMap = mutableMapOf<String, String>()
        trackMap[PropertyName.APP_NAME] = "Dainik Bhaskar"
        Tracking.trackEvent(
            EventName.FIRST_LAUNCH, trackMap,
            superPropertyMap = mapOf(Pair(PropertyName.LOGIN_STATUS, "Non Logged In")),
            trackRequestOption = TrackingOptions(firebase = true, comscore = false)
        )

        //Tracking.trackUserIdentity("123", TrackingOptions(true,false))
        tvWelcomeText.setOnClickListener{

            // Tracking
            val trackMap = mutableMapOf<String, String>()
            trackMap[PropertyName.APP_NAME] = "JANS-SocietyOO"
            Tracking.trackEvent("app_open", trackMap,
                superPropertyMap = mapOf(Pair(PropertyName.LOGIN_STATUS, "Non Logged In")),
                trackRequestOption = TrackingOptions(firebase = true, comscore = false)
            )
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //startActivity(Intent(this, LoginActivity::class.java))
    }
}
