package com.jans.societyoo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager


class Constants {


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

}