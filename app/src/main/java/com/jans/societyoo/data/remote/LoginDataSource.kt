package com.jans.societyoo.data.remote

import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    var jsonServices: JsonApi = RetrofitInstance.jsonServices
    var jsonServicesTest: JsonApi = RetrofitInstance.jsonServicesTest

    suspend fun sendOTP(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>> = tryCatching {
        jsonServices.sendOTP(otpRequest)
    }
    suspend fun verifyOTP(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.verifyOTP(otpVerifyRequest)
    }

    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.updateUserProfile(userDetail)
    }

    suspend fun getUser(userId: Int): MyResult<User> = tryCatching {
        jsonServicesTest.getUser(userId)
    }

    suspend fun getUserList(): MyResult<List<User>> = tryCatching {
        jsonServicesTest.getUserList()
    }

    suspend fun postUserData(userPostData: UserPostData): MyResult<UserData> = tryCatching {
        jsonServicesTest.postUserData(userPostData)
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

