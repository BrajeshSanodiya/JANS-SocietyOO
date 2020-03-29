package com.jans.societyoo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity


object Constants {
   /* companion object{
       // var autoOTPSendAllow:Boolean=false
    }*/
    /*object Endpoints {
        const val BASE_ENDPOINT = "http://prod.bhaskarapi.com/"
    }

    object Headers {
        const val KEY_APP_VERSION_CODE = "a-ver-code"
        const val KEY_APP_VERSION_NAME = "a-ver-name"
        const val KEY_X_AUTHENTICATION_TOKEN = "x-aut-t"
        const val KEY_CHANNEL_ID = "cid"
        const val KEY_DEVICE_TYPE = "dtyp"
        const val KEY_AUTHENTICATION_TOKEN = "at"

        const val VALUE_DEVICE_TYPE = "android"
        const val VALUE_X_AUTHENTICATION_TOKEN = "a6oaq3edtz59"
        const val MAX_AUTH_RETRY = 2
    }*/


  /*  object ErrorMessage {
        const val UNKNOWN_ERROR = "An unknown error occurred!"
        const val NO_SUCH_DATA = "Data not found in the database"
    }*/


    fun isNetworkAvailable(ctx: Context): Boolean {
        val connectivityManager =
            ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showAlert(message: String?, context: Activity?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(message).setCancelable(false)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id -> })
        try {
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openWhatsApp(context: Context, mobileNo:String) {
        val smsNumber = mobileNo
        val isWhatsappInstalled = whatsappInstalledOrNot(context,"com.whatsapp")
        if (isWhatsappInstalled) {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.putExtra(
                "jid",
                PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net"
            ) //phone number without "+" prefix
            context.startActivity(sendIntent)
        } else {
            val uri: Uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            PrintMsg.toast(context, "WhatsApp not Installed")
            context.startActivity(goToMarket)
        }
    }

    fun whatsappInstalledOrNot(context: Context,uri: String): Boolean {
        val pm: PackageManager = context.getPackageManager()
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }
}