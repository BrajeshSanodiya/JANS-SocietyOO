package com.jans.societyoo.data.repository

import android.content.Context
import com.jans.societyoo.data.local.db.LoginDataSourceDB
import com.jans.societyoo.data.remote.LoginDataSource
import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.SingleRunner

class LoginRepository(context: Context) {

    var loginDataSourceDB:LoginDataSourceDB= LoginDataSourceDB(context)

    var loginDataSource:LoginDataSource= LoginDataSource()

    val singleRunner = SingleRunner()
    suspend fun sendOtp(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>>{
        return loginDataSource.sendOTP(otpRequest)
    }

    suspend fun getAllFlatsDB(): List<FlatDetail> {
            return loginDataSourceDB.getAllFlats()
    }

    suspend fun getUserDetailDB(): UserDetail {
        return loginDataSourceDB.getUserDetail()
    }

    suspend fun setUserDetailDB(userDetail: UserDetail) {
        return loginDataSourceDB.insertUserDetail(userDetail)
    }
    suspend fun deleteAllUsersDB() {
        return loginDataSourceDB.deleteAllUsers()
    }

    suspend fun deleteAllFlatsDB() {
        return loginDataSourceDB.deleteAllFlats()
    }

    suspend fun setFlatDetailsDB(flatDetails: List<FlatDetail>){
        return loginDataSourceDB.insertAllFlats(flatDetails)
    }

    suspend fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>>{
        var result:MyResult<ApiDataObject<UserData>> =  loginDataSource.verifyOTP(otpVerifyRequest)
        return result
    }

    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>>{
       var result:MyResult<ApiDataObject<UserData>> =  loginDataSource.updateUserProfile(userDetail)
        return result
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

