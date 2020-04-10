package com.jans.societyoo.ui.onboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.jans.onboarding.OnBoardingScreen
import com.jans.onboarding.OnBoardingView
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.ui.login.LoginActivity
import com.jans.societyoo.ui.navigation.MainActivity
import com.jans.societyoo.utils.PrintMsg


class OnBoardActivity : AppCompatActivity() {
    var preferences = UserPreferences(this)
    private fun callNextActivity() {
        UserPreferences::appOpenFirstTime.set(preferences,true)
        if (TextUtils.isEmpty(preferences.mobileNum)) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        val onBoardingView: OnBoardingView = findViewById(R.id.onboardingView)
        val arrayTitle = resources.getStringArray(R.array.feature_title)
        val arrayDesc = resources.getStringArray(R.array.feature_desc)
        var screens =
            arrayListOf<OnBoardingScreen>()  //OnBoardingScreen(titleText: String,subTitleText:String,drawableResId:Int?,screenBGColor: Int)
        screens.add(
            OnBoardingScreen(
                titleText = arrayTitle[0],
                subTitleText = arrayDesc[0],
                drawableResId = R.drawable.feature1,
                screenBGColor = Color.WHITE
            )
        )
        screens.add(
            OnBoardingScreen(
                titleText = arrayTitle[1],
                subTitleText = arrayDesc[1],
                drawableResId = R.drawable.feature2,
                screenBGColor = Color.WHITE //Color.parseColor("#F44336")
            )
        )
        screens.add(
            OnBoardingScreen(
                titleText = arrayTitle[2],
                subTitleText = arrayDesc[2],
                drawableResId = R.drawable.feature3,
                screenBGColor = Color.WHITE
            )
        )
        screens.add(
            OnBoardingScreen(
                titleText = arrayTitle[3],
                subTitleText = arrayDesc[3],
                screenBGColor = Color.WHITE,
                drawableResId = R.drawable.feature4
            )
        )

        onBoardingView.setScreens(screens)

        onBoardingView.onEnd {
            //finish()
            PrintMsg.toastDebug(this, "OnBoarding Finished")
            callNextActivity()
        }

        onBoardingView.onFinish {
            PrintMsg.toastDebug(this, "OnBoarding last screen")
            /* return false will not trigger this action again(on swipe back ) ,
            true will trigger it with every swipe to last screen ,
            may used for showing some animation or something */
            return@onFinish false

        }
    }
}
