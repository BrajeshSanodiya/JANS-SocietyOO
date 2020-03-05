package com.jans.societyoo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.jans.societyoo.ui.login.*
import com.my.retrodemo1.LoginRepository


class LoginViewModel() : ViewModel() {
    private val loginRepository: LoginRepository = LoginRepository();
    val mobileNumberLiveData = MutableLiveData<String>()

    private val _loginMobileViewState = MutableLiveData<LoginMobileViewState>()
    val loginMobileViewState: LiveData<LoginMobileViewState> = _loginMobileViewState

    private val _loginOtpViewState = MutableLiveData<LoginOtpViewState>()
    val loginOtpViewState: LiveData<LoginOtpViewState> = _loginOtpViewState

    private val _loginViewState = MutableLiveData<LoginViewState>()
    val loginViewState: LiveData<LoginViewState> = _loginViewState

    fun setMobileNumberLiveData(mobile:String){
        mobileNumberLiveData.value=mobile
    }



    fun openAfterLoginScreen() {
            loginFragmentChanged(LoginFragmentState.AFTER_LOGIN)
    }


    fun openOtpScreen(mobile: String) {
        if (isMobileValid(mobile)) {
            setMobileNumberLiveData(mobile)
            loginFragmentChanged(LoginFragmentState.OTP_VERIFY)
        }
    }
    fun mobileDataChanged(mobile: String) {
        if (isMobileValid(mobile)) {
            _loginMobileViewState.value =
                LoginMobileViewState(isDataValid = true)
        }else{
            _loginMobileViewState.value =
                LoginMobileViewState(isDataValid = false)
        }
    }
    fun showMobileNextButton(mobile: String) {
            _loginMobileViewState.value =
                LoginMobileViewState(
                    isDataValid = isMobileValid(mobile)
                )
    }
    fun showOtpNextButton(otpValue:String?, isFilled: Boolean) {
        _loginOtpViewState.value = LoginOtpViewState(
            otpValue = otpValue,
            isDataValid = isFilled
        )
    }
    fun hideNextButton() {
        _loginOtpViewState.value =
            LoginOtpViewState(isDataValid = false)
    }
    fun otpResend() {
        _loginOtpViewState.value =
            LoginOtpViewState(isOtpResend = true)
    }

    fun loginFragmentChanged(loginState: Int) {
        _loginViewState.value =
            LoginViewState(loginState)
    }

    private fun isMobileValid(mobile: String): Boolean {
        return mobile.length ==10
    }


    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder username validation check
    private fun isNameValid(username: String): Boolean {
        return username.length > 5
    }


    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }



}
