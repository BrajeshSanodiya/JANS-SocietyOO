package com.jans.societyoo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.liveData
import com.jans.societyoo.model.login.OtpRequest
import com.jans.societyoo.model.login.OtpVerifyRequest
import com.jans.societyoo.ui.login.*
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.data.repository.LoginRepository
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail


class LoginViewModel() : ViewModel() {
    private val loginRepository: LoginRepository = LoginRepository();
    val mobileNumberLiveData = MutableLiveData<String>()
    //val flatsDataLiveData = MutableLiveData<List<Flat>>()
    val flatsDetailLiveData=MutableLiveData<List<FlatDetail>>()
    val userDetailLiveData=MutableLiveData<UserDetail>()

    val loginFlatViewState = MutableLiveData<LoginFlatsViewState>()


    private val _loginMobileViewState = MutableLiveData<LoginMobileViewState>()
    val loginMobileViewState: LiveData<LoginMobileViewState> = _loginMobileViewState

    private val _loginOtpViewState = MutableLiveData<LoginOtpViewState>()
    val loginOtpViewState: LiveData<LoginOtpViewState> = _loginOtpViewState

    private val _loginViewState = MutableLiveData<LoginViewState>()
    val loginViewState: LiveData<LoginViewState> = _loginViewState

    fun sendOtp(mobile: String) = liveData {
        var otpRequest= OtpRequest(mobile)
        val result = loginRepository.sendOtp(otpRequest)
        PrintMsg.println("API Response : sendOtp : ${result.toString()}")
        emit(result)
    }

    fun verifyOtp(mobile: String,otpValue: String) = liveData {
        var otpVerifyRequest=OtpVerifyRequest(mobile,otpValue)
        val result = loginRepository.verifyOtp(otpVerifyRequest)
        PrintMsg.println("API Response : verifyOtp : ${result.toString()}")
        emit(result)
    }

    fun setFlatsConfirm(selectedId:Int,checked:Boolean){
        loginFlatViewState.value=LoginFlatsViewState(selectedFlatId=selectedId,isItemChecked = checked)
    }
    fun setFlatsUsers(flats:List<FlatDetail>, userDetail: UserDetail,mobile: String){
        //loginFlatViewState.value=LoginFlatsViewState(flats=flats,userDetail = userDetail)
        flatsDetailLiveData.value=flats
        userDetailLiveData.value=userDetail
        mobileNumberLiveData.value=mobile
    }


    fun setMobileNumberLiveData(mobile:String){
        mobileNumberLiveData.value=mobile
    }


    fun openAfterLoginScreen() {
        loginFragmentChanged(LoginFragmentState.AFTER_LOGIN)
    }
    /*fun callAfterLoginScreen() {
        _loginViewState.value =LoginViewState(loginState)
    }
*/
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
                LoginMobileViewState(true,isDataValid = false)
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
