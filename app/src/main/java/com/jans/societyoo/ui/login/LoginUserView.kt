package com.jans.societyoo.ui.login

/**
 * User details post authentication that is exposed to the UI
 */

data class LoginUserView(
    val userID: String?=null,
    val displayName: String?=null,
    val mobileNumber: String?=null,
    val emailID: String?=null
    //... other data fields that may be accessible to the UI
)
/*

data class MobileOtpUserView(
    val mobileNumber: String
)*/
