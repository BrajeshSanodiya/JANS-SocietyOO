package com.jans.societyoo.data.remote

import com.jans.loginsample.data.model.LoginModel
import com.jans.loginsample.data.model.MobileOtpModel

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var login: LoginModel? = null
        private set
    var mobileOtp:MobileOtpModel?=null
        private set

    val isLoggedIn: Boolean
        get() = login != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        login = null
        mobileOtp=null
    }

    fun logout() {
        login = null
        dataSource.logout()
    }

    fun mobileOTP(mobile: String): Result<MobileOtpModel> {
        // handle login
        val result = dataSource.mobileOTP(mobile)

        if (result is Result.Success) {
            //setLoggedInUser(result.data)
        }

        return result
    }

    /*private fun setLoggedInUser(loggedInUser: LoginModel) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }*/
}
