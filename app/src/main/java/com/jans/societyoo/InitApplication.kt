package com.jans.societyoo

import android.app.Application
import com.jans.tracking.Tracking

class InitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Preferences2.init(this)

        var appSignature = AppSignatureHelper(this)
        appSignature.appSignatures
        println("appSignature : "+appSignature.appSignatures.toString())
        Tracking.initializeTracking(this,"","",getString(R.string.app_name))
    }
}