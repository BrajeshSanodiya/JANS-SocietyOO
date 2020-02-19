package com.jans.societyoo.data.remote

import com.jans.loginsample.data.model.LoginModel
import com.jans.loginsample.data.model.MobileOtpModel
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun mobileOTP(mobile:String): ResultOld<MobileOtpModel> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = MobileOtpModel(mobile)
            return ResultOld.Success(fakeUser)
        } catch (e: Throwable) {
            return ResultOld.Error(IOException("Error logging in", e))
        }
    }

    fun login(userId:String,displayName:String,mobileNumber:String,emailID:String): ResultOld<LoginModel> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoginModel(userId,displayName,mobileNumber,emailID)
            return ResultOld.Success(fakeUser)
        } catch (e: Throwable) {
            return ResultOld.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

