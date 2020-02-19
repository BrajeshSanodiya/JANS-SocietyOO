package com.my.retrodemo1

import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching
import com.my.retrodemo1.retrofit.Album
import com.my.retrodemo1.retrofit.JsonApi
import com.my.retrodemo1.retrofit.RetrofitInstance

class UserRepository {

    var jsonServices: JsonApi = RetrofitInstance.jsonServices

    suspend fun getUser(userId: Int): MyResult<User> = tryCatching {
        jsonServices.getUser(userId)
    }

    suspend fun getUserList(): MyResult<List<User>> = tryCatching {
        jsonServices.getUserList()
    }

    suspend fun postUserData(userPostData: UserPostData): MyResult<UserData> = tryCatching {
        jsonServices.postUser(userPostData)
    }
}

