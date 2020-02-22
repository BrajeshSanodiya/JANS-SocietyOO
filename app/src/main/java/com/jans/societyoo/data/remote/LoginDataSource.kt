package com.jans.societyoo.data.remote

import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching
import com.my.retrodemo1.retrofit.JsonApi
import com.my.retrodemo1.retrofit.RetrofitInstance

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    var jsonServices: JsonApi = RetrofitInstance.jsonServices

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

