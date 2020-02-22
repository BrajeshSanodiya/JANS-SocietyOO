package com.my.retrodemo1

import com.jans.societyoo.data.remote.LoginDataSource
import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching
import com.my.retrodemo1.retrofit.Album
import com.my.retrodemo1.retrofit.JsonApi
import com.my.retrodemo1.retrofit.RetrofitInstance

class LoginRepository {

    var loginDataSource:LoginDataSource= LoginDataSource()

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

