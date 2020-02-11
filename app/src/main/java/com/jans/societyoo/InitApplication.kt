package com.jans.societyoo

import android.app.Application
import com.jans.societyoo.data.local.prefs.Preferences2
import com.jans.tracking.Tracking

class InitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Preferences2.init(this)

        Tracking.initializeTracking(this,"","",getString(R.string.app_name))
    }
}