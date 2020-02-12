package com.jans.societyoo.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFieldState(
    val nameError: Int? = null,
    val emailError: Int? = null,
    val mobileNumberError: Int? = null,
    val isDataValid: Boolean = false,
    val loginState:Int?=null
)

data class LoginEventState(
    val fragmentState:Int
    //, val isDataValid: Boolean
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
