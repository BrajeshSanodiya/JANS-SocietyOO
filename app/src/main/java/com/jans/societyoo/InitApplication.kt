package com.jans.societyoo

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.jans.tracking.Tracking


class InitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Preferences2.init(this)

        // FB Stetho
        if (BuildConfig.DEBUG ) {
            Stetho.initializeWithDefaults(this)
        }
        // FB Flipper
        SoLoader.init(this, false);
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(NetworkFlipperPlugin())
            client.addPlugin(DatabasesFlipperPlugin(this))
            client.start()
        }


        var appSignature = AppSignatureHelper(this)
        appSignature.appSignatures
        println("appSignature : "+appSignature.appSignatures.toString())
        Tracking.initializeTracking(this,"","",getString(R.string.app_name))
    }
}