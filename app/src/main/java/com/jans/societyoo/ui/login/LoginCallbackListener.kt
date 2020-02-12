package com.jans.societyoo.ui.login

interface LoginCallbackListener {

    fun onMobileOTPSend(mobileNumber: String)

    fun onMobileOtpResend()

    fun onMobileOtpVerify(application: String?, otp: String?)
}