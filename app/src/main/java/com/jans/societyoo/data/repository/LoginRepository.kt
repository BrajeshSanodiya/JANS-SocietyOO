package com.jans.societyoo.data.repository

import com.jans.societyoo.data.remote.LoginDataSource
import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.utils.MyResult

class LoginRepository {

    var loginDataSource:LoginDataSource= LoginDataSource()

    suspend fun sendOtp(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>>{
        return loginDataSource.sendOTP(otpRequest)
    }

    suspend fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>>{
        return loginDataSource.verifyOTP(otpVerifyRequest)
    }

    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>>{
        return loginDataSource.updateUserProfile(userDetail)
    }



    suspend fun getUser(userId: Int): MyResult<User>{
        return loginDataSource.getUser(userId)
    }

    suspend fun getUserList(): MyResult<List<User>>{
        return  loginDataSource.getUserList()
    }

    suspend fun postUserData(userPostData: UserPostData): MyResult<UserData> {
        return  loginDataSource.postUserData(userPostData)
    }
}

