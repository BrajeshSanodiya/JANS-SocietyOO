package com.jans.societyoo.ui.splash

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jans.roomexplorer.RoomExplorer
import com.jans.societyoo.R
import com.jans.societyoo.data.local.db.DatabaseInstance
import kotlinx.android.synthetic.main.activity_splash_db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DatabaseSplashActivity : AppCompatActivity() {
    private fun callNextActivity() {
        RoomExplorer.show(this, DatabaseInstance::class.java, DatabaseInstance.DB_NAME)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_db)

        tvWelcome_splash_db.text = resources.getString(R.string.welcome_splash, resources.getString(R.string.db_app_name))

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            callNextActivity()
        }

    }
}
