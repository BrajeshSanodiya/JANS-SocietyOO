package com.jans.societyoo.data.local.prefs

import android.content.Context
import com.jans.societyoo.model.login.UserDetail


class UserPreferences(context: Context) : Preferences(context){
    /*var flatsDetail by stringPref()
    var userDetail by stringPref()*/

    var mobileNumV2 by stringPref()
    var appOpenFirstTimeV2 by booleanPref()
    var defaultUserId by intPref()
    var defaultFlatId by intPref()



    var userName by stringPref()
    var emailAccount by stringPref()
    var societyName by stringPref()
}