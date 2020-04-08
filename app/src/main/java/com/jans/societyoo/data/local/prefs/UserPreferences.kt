package com.jans.societyoo.data.local.prefs

import android.content.Context
import com.jans.societyoo.model.login.UserDetail


class UserPreferences(context: Context) : Preferences(context){
    /*var flatsDetail by stringPref()
    var userDetail by stringPref()*/

    var mobileNum by stringPref()
    var appOpenFirstTime by booleanPref()
    var defaultUserId by intPref()
    var defaultFlatId by intPref()



    var userName by stringPref()
    var emailAccount by stringPref()
    var societyName by stringPref()
}