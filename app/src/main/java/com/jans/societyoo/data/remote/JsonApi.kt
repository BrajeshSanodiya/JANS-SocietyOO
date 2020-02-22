package com.my.retrodemo1.retrofit

import com.jans.societyoo.model.User
import com.jans.societyoo.model.UserData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.utils.MyResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL = "https://jsonplaceholder.typicode.com"

interface JsonApi {

    @GET("/users/{id}")
    suspend fun getUser(@Path(value = "id") userId: Int): User

    @GET("/users")
    suspend fun getUserList(): List<User>

    @POST("/posts")
    suspend fun postUserData(@Body userPostData: UserPostData): UserData
}