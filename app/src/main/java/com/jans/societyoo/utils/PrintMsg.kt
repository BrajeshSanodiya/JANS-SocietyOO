package com.jans.societyoo.utils

import android.content.Context
import android.widget.Toast

object PrintMsg {

    fun toastDebug(context: Context?, message:String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun toast(context: Context?, message:String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun printlnDebug(message:String){
        kotlin.io.println(message)
    }
    fun println(message:String){
        kotlin.io.println(message)
    }
}