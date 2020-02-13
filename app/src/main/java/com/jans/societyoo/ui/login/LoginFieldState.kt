package com.jans.societyoo.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginMobileViewState(
    val mobileNumberError: Int? = null,
    val isDataValid: Boolean = false
)

data class LoginOtpViewState(
    val otpValue:String?=null,
    val otpError: Int? = null,
    val isDataValid: Boolean = false
)
data class LoginViewState(
    val fragmentState:Int
)

class LoginState{
companion object{
    val PRE_REGISTRATION:Int=1021
    val MOBILE_INPUT:Int=1032
    val OTP_VERIFY:Int=1043
    val REGISTRATION:Int=1054
}
}
/*
data class MobileFormState(
    val mobileNumberError: Int? = null,
    val isDataValid: Boolean = false
)*/
