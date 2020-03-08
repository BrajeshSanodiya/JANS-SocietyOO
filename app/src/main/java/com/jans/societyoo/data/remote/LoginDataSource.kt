package com.jans.societyoo.data.remote

import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.OTPVerifyData
import com.jans.societyoo.model.login.OtpRequest
import com.jans.societyoo.model.login.OtpVerifyRequest
import com.jans.societyoo.model.login.SendOTPData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    var jsonServices: JsonApi = RetrofitInstance.jsonServices

    suspend fun sendOTP(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>> = tryCatching {
        jsonServices.sendOTP(otpRequest)
    }
    suspend fun verifyOTP(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<OTPVerifyData>> = tryCatching {
        jsonServices.verifyOTP(otpVerifyRequest)
    }

    suspend fun getUser(userId: Int): MyResult<User> = tryCatching {
        jsonServices.getUser(userId)
    }

    suspend fun getUserList(): MyResult<List<User>> = tryCatching {
        jsonServices.getUserList()
    }

    suspend fun postUserData(userPostData: UserPostData): MyResult<UserData> = tryCatching {
        jsonServices.postUserData(userPostData)
    }

    /*fun mobileOTP(mobile:String): ResultOld<MobileOtpModel> {
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
    }*/
}

