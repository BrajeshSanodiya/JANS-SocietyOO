package com.jans.societyoo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.jans.societyoo.R
import com.jans.societyoo.data.remote.LoginRepository
import com.jans.societyoo.data.remote.Result


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginFormState = MutableLiveData<LoginFieldState>()
    val loginFormState: LiveData<LoginFieldState> = _loginFormState

    private val _loginEventState = MutableLiveData<LoginEventState>()
    val loginEventState: LiveData<LoginEventState> = _loginEventState

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun mobile(mobile: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.mobileOTP(mobile)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoginUserView(mobileNumber = result.data.mobileNumber))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun mobileDataChanged(mobile: String) {
        if (!isMobileValid(mobile)) {
            _loginFormState.value = LoginFieldState(mobileNumberError = R.string.invalid_mobile,loginState = LoginState.MOBILE_INPUT)
        } else {
            _loginFormState.value = LoginFieldState(isDataValid = true,loginState = LoginState.MOBILE_INPUT)
        }
    }

    fun loginFragmentChanged(loginState: Int) {
       /* if (!isMobileValid(mobile)) {
            _loginForm.value = LoginFieldState(mobileNumberError = R.string.invalid_mobile,loginState = LoginState.MOBILE_INPUT)
        } else {
            _loginForm.value = LoginFieldState(isDataValid = true,loginState = LoginState.MOBILE_INPUT)
        }*/
        _loginEventState.value = LoginEventState(loginState)
    }

    // A placeholder password validation check
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
