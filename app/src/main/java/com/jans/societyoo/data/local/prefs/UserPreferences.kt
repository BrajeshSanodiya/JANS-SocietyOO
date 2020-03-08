package com.jans.societyoo.data.local.prefs

import android.content.Context


class UserPreferences(context: Context) : Preferences(context){
    var flats by stringPref()
    var userName by stringPref()
    var emailAccount by stringPref()
    var mobileNum by stringPref()
    var societyName by stringPref()
}