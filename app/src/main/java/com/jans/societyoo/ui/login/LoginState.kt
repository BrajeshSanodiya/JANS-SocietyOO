package com.jans.societyoo.ui.login

import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail


data class LoginMobileViewState(
    val mobileNumberError: Boolean = false,
    val isDataValid: Boolean = false
)

data class LoginOtpViewState(
    val otpValue: String? = null,
    val otpError: Int? = null,
    val isOtpResend: Boolean = false,
    val isDataValid: Boolean = false,
    val mobileNumber: String? = null
)

data class LoginFlatsViewState(
    /*val flats: List<FlatDetail>? = null,
    val userDetail: UserDetail? = null,*/
    val selectedUserId: Int = 0,
    val isItemChecked: Boolean = false
)

data class LoginUserProfileViewState (
    val validName: Boolean = false,
    val validEmail: Boolean = false,
    val validGender:Boolean=false,
    val validDOB: Boolean=false,
    val errorName: Boolean = false,
    val errorEmail: Boolean = false,
    val errorGender:Boolean=false,
    val errorDOB:Boolean=false

)


data class LoginViewState(
    val fragmentState: Int
)

class LoginFragmentState {
    companion object {
        val MOBILE_INPUT: Int = 1
        val OTP_VERIFY: Int = 2
        val FLAT_CONFIRM: Int = 3
        val USER_PROFILE: Int = 4
        val AFTER_LOGIN: Int = 5
    }
}
