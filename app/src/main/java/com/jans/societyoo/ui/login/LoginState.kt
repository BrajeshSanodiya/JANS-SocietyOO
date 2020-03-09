package com.jans.societyoo.ui.login

import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail


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
data class LoginFlatsViewState(
    val flats:List<FlatDetail>?=null,
    val userDetail: UserDetail?=null,
    val selectedFlatId:Int?=null,
    val isItemChecked: Boolean = false
)

data class LoginUserProfileViewState(
    val mobileNumberError: Int? = null,
    val isDataValid: Boolean = false
)


data class LoginViewState(
    val fragmentState:Int
)

class LoginFragmentState{
companion object{
    val PRE_REGISTRATION:Int=1
    val MOBILE_INPUT:Int=2
    val OTP_VERIFY:Int=3
    val FLAT_CONFIRM:Int=4
    val USER_PROFILE:Int=5
    val AFTER_LOGIN:Int=6
}
}
