package com.jans.societyoo.ui.login


data class LoginMobileViewState(
    val mobileNumberError: Int? = null,
    val isDataValid: Boolean = false
)

data class LoginOtpViewState(
    val otpValue:String?=null,
    val otpError: Int? = null,
    val isOtpResend: Boolean = false,
    val isDataValid: Boolean = false,
    val mobileNumber: String?=null
)
data class LoginViewState(
    val fragmentState:Int
)

class LoginFragmentState{
companion object{
    val PRE_REGISTRATION:Int=1021
    val MOBILE_INPUT:Int=1032
    val OTP_VERIFY:Int=1043
    val AFTER_LOGIN:Int=1054
}
}
